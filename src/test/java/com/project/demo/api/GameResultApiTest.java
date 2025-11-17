package com.project.demo.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameResultApiTest {

    private static String token;
    private static Long userId;
    private static Long createdGameId;
    private static Long createdResultId;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";

        String body = """
        {
          "email": "SuperAdmin01@gmail.com",
          "password": "SuperAdmin"
        }
        """;

        Response loginRes =
                given()
                        .header("Content-Type", "application/json")
                        .body(body)
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        token = loginRes.jsonPath().getString("token");
        userId = loginRes.jsonPath().getLong("authUser.id");
    }

    @Test
    @Order(1)
    public void CreateGameForResults() {

        String body = """
        {
          "name": "Juego para resultados",
          "description": "desc",
          "type_exercise": "prueba"
        }
        """;

        Response res =
                given()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .body(body)
                        .when()
                        .post("/games")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        createdGameId = res.jsonPath().getLong("gameId");

        assertThat("El gameId no debería ser nulo",
                createdGameId, notNullValue());
    }

    @Test
    @Order(2)
    public void CreateGameResult() {

        long now = new Date().getTime();

        String body = """
        {
          "gameDate": %d,
          "score": 120,
          "time": 45,
          "levelDifficulty": "MEDIUM",
          "userId": { "id": %d },
          "gameId": { "gameId": %d }
        }
        """.formatted(now, userId, createdGameId);

        Response res =
                given()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .body(body)
                        .when()
                        .post("/gameResults")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        createdResultId = res.jsonPath().getLong("resultId");

        assertThat("El resultId no debería ser nulo",
                createdResultId, notNullValue());
    }

    @Test
    @Order(3)
    public void GetAllGameResults() {

        Response res =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/gameResults")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        assertThat("La lista de resultados no debe venir nula.",
                res.jsonPath().getList("$"), notNullValue());
    }

    @Test
    @Order(4)
    public void GetGameResultById() {

        Response res =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/gameResults/" + createdResultId)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        Long returned = res.jsonPath().getLong("resultId");

        assertThat(
                "El ID del resultado devuelto no coincide.",
                returned,
                equalTo(createdResultId)
        );
    }

    @Test
    @Order(5)
    public void GetGameResultsByUserId() {

        Response res =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/gameResults/user/" + userId)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        assertThat(
                "No devolvió ningún resultado para el usuario.",
                res.jsonPath().getList("$").size(),
                greaterThan(0)
        );
    }

    @Test
    @Order(6)
    public void DeleteGameResult() {

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/gameResults/" + createdResultId)
                .then()
                .statusCode(200);

        // Validar que ya NO existe
        Response res =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/gameResults/" + createdResultId)
                        .then()
                        .extract()
                        .response();

        assertThat(
                "El resultado aún existe después de ser eliminado.",
                res.statusCode(),
                is(anyOf(is(404), is(500)))
        );
    }

}
