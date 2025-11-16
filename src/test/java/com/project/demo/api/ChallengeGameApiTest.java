package com.project.demo.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChallengeGameApiTest {

    private static String adminToken;
    private static String userToken;

    private static Long badgeId;
    private static Long gameId;
    private static Long challengeId;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";

        // LOGIN ADMIN
        String adminLogin = """
        {
          "email": "SuperAdmin01@gmail.com",
          "password": "SuperAdmin"
        }
        """;

        Response adminResponse = given()
                .header("Content-Type", "application/json")
                .body(adminLogin)
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().response();

        adminToken = adminResponse.jsonPath().getString("token");

        // LOGIN USER (NECESARIO PARA NEGATIVAS)
        String userLogin = """
        {
          "email": "User01@gmail.com",
          "password": "User"
        }
        """;

        Response userResponse = given()
                .header("Content-Type", "application/json")
                .body(userLogin)
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().response();

        userToken = userResponse.jsonPath().getString("token");
    }

    @Test
    @Order(1)
    public void CreateBadge() {

        String badgeJson = """
        {
            "title": "Badge Test",
            "description": "Descripción automática",
            "url": null
        }
        """;

        Response res = given()
                .header("Authorization", "Bearer " + adminToken)
                .multiPart("badge", badgeJson)
                .post("/badges")
                .then()
                .statusCode(200)
                .extract().response();

        badgeId = res.jsonPath().getLong("badgeId");

        assertThat("No devolvió badgeId válido", badgeId, notNullValue());
    }

    @Test
    @Order(2)
    public void CreateGame() {

        String body = """
        {
            "name": "Juego Test",
            "description": "Juego generado automáticamente",
            "type_exercise": "Test Type"
        }
        """;

        Response res = given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(body)
                .post("/games")
                .then()
                .statusCode(200)
                .extract().response();

        gameId = res.jsonPath().getLong("gameId");

        assertThat("GameId devuelto es nulo", gameId, notNullValue());
    }

    @Test
    @Order(3)
    public void CreateChallengeGame() {

        String challenge = """
        {
          "title": "Challenge Automático",
          "description": "Descripción pruebas",
          "startDate": "2025-01-01T00:00:00.000+00:00",
          "endDate": "2025-12-31T00:00:00.000+00:00",
          "objectiveScore": 50,
          "objectiveTime": 30,
          "objectiveFrecuency": 5,
          "badgeId": { "badgeId": %d },
          "gameId": { "gameId": %d }
        }
        """.formatted(badgeId, gameId);

        Response res = given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(challenge)
                .post("/challengeGame")
                .then()
                .statusCode(200)
                .extract().response();

        challengeId = res.jsonPath().getLong("challengeId");

        assertThat("ChallengeGame no devolvió ID", challengeId, notNullValue());
    }

    @Test
    @Order(4)
    public void GetActiveChallenges() {

        Response res = given()
                .header("Authorization", "Bearer " + adminToken)
                .get("/challengeGame/challenges?status=active")
                .then()
                .statusCode(200)
                .extract().response();

        assertThat("Active challenges debe devolver lista",
                res.jsonPath().getList("$"), is(notNullValue()));
    }

    @Test
    @Order(5)
    public void UpdateChallengeDates() {

        String update = """
        {
          "startDate": "2026-01-01T00:00:00.000+00:00",
          "endDate": "2026-12-31T00:00:00.000+00:00"
        }
        """;

        Response res = given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(update)
                .put("/challengeGame/" + challengeId)
                .then()
                .statusCode(200)
                .extract().response();

        assertThat("La fecha no cambió",
                res.jsonPath().getString("startDate"), containsString("2026"));
    }


    @Test
    @Order(7)
    public void UserCreateChallengeFail() {

        String body = """
        {
          "title": "Prohibido",
          "description": "Debe fallar",
          "startDate": "2025-01-01T00:00:00.000+00:00",
          "endDate": "2025-12-31T00:00:00.000+00:00",
          "objectiveScore": 10,
          "objectiveTime": 10,
          "objectiveFrecuency": 1,
          "badgeId": { "badgeId": %d },
          "gameId": { "gameId": %d }
        }
        """.formatted(badgeId, gameId);

        Response res = given()
                .header("Authorization", "Bearer " + userToken)
                .header("Content-Type", "application/json")
                .body(body)
                .post("/challengeGame")
                .then()
                .extract().response();

        assertThat("User debería recibir 403",
                res.statusCode(), is(403));
    }

    @Test
    @Order(6)
    public void UpdateChallengeWithUserFail() {

        String updateBody = """
    {
      "startDate": "2025-11-16T00:00:00.000+00:00",
      "endDate": "2025-12-31T00:00:00.000+00:00"
    }
    """;

        Response res = given()
                .header("Authorization", "Bearer " + userToken)
                .header("Content-Type", "application/json")
                .body(updateBody)
                .when()
                .put("/challengeGame/" + challengeId)
                .then()
                .extract()
                .response();

        // ESTO DEBERÍA PASAR (403)
        //    pero dará 200
        assertThat(
                "Un usuario normal NO debería poder actualizar un ChallengeGame.",
                res.statusCode(),
                is(403)
        );
    }

    @Test
    @Order(8)
    public void InvalidStatusFail() {

        Response res = given()
                .header("Authorization", "Bearer " + adminToken)
                .get("/challengeGame/challenges?status=INVALIDO")
                .then()
                .extract().response();

        assertThat("Debe fallar con 400 o 500",
                res.statusCode(), is(anyOf(is(400), is(500))));
    }

    @Test
    @Order(9)
    public void DeleteChallengeGame() {

        given()
                .header("Authorization", "Bearer " + adminToken)
                .delete("/challengeGame/" + challengeId)
                .then()
                .statusCode(200);
    }
}
