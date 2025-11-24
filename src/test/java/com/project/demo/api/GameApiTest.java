package com.project.demo.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameApiTest {

    @LocalServerPort
    private int port;

    private static String token;
    private static Long createdGameId;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        String loginBody = """
        {
          "email": "SuperAdmin01@gmail.com",
          "password": "SuperAdmin"
        }
        """;

        Response loginResponse = given()
                .header("Content-Type", "application/json")
                .body(loginBody)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .response();

        token = loginResponse.jsonPath().getString("token");
    }

    @Test
    @Order(1)
    public void GetAllGames() {

        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/games")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        assertThat("La lista de juegos NO debe ser nula.",
                response.jsonPath().getList("$"), notNullValue());

        assertThat("La lista de juegos debería tener elementos.",
                response.jsonPath().getList("$").size(), greaterThan(0));
    }

    @Test
    @Order(2)
    public void CreateGame() {

        String gameBody = """
        {
          "name": "Juego de Prueba",
          "description": "Descripción generada automáticamente",
          "type_exercise": "Cognitivo"
        }
        """;

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .body(gameBody)
                        .when()
                        .post("/games")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        createdGameId = response.jsonPath().getLong("gameId");

        assertThat("El gameId no debería ser nulo",
                createdGameId, notNullValue());

        assertThat("El nombre del juego no coincide",
                response.jsonPath().getString("name"),
                equalTo("Juego de Prueba"));
    }

    @Test
    @Order(3)
    public void GetGameById() {

        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/games/" + createdGameId)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        assertThat("El ID obtenido no coincide",
                response.jsonPath().getLong("gameId"),
                equalTo(createdGameId));

        assertThat("El nombre del juego devuelto no es correcto",
                response.jsonPath().getString("name"),
                equalTo("Juego de Prueba"));
    }

    @Test
    @Order(4)
    public void DeleteGame() {

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/games/" + createdGameId)
                .then()
                .statusCode(200);

        // Verificación posterior
        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/games/" + createdGameId)
                        .then()
                        .extract()
                        .response();

        assertThat(
                "El juego debería haber sido eliminado.",
                response.statusCode(),
                anyOf(is(404), is(500))
        );
    }

    @Test
    @Order(5)
    public void CreateGameWithoutToken() {

        String gameBody = """
        {
          "name": "Juego sin token",
          "description": "No debería crearse",
          "type_exercise": "Test"
        }
        """;

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .body(gameBody)
                        .when()
                        .post("/games")
                        .then()
                        .extract()
                        .response();

        assertThat(
                "Sin token debería devolver 403.",
                response.statusCode(),
                is(403)
        );
    }

    @Test
    @Order(6)
    public void GetNonExistentGame() {

        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/games/999999999")
                        .then()
                        .extract()
                        .response();

        assertThat(
                "Un juego inexistente debería devolver error.",
                response.statusCode(),
                anyOf(is(404), is(500))
        );
    }

    @Test
    @Order(7)
    public void DeleteNonExistentGame() {

        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .delete("/games/999999999")
                        .then()
                        .extract()
                        .response();

        assertThat(
                "Borrar juego inexistente debería devolver 200 porque el backend no valida existencia.",
                response.statusCode(),
                is(200)
        );
    }
}
