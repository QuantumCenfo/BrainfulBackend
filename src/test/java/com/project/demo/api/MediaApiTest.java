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
public class MediaApiTest {

    @LocalServerPort
    private int port;

    private static String adminToken;
    private static String userToken;
    private static Long createdMediaId;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        // Login ADMIN
        String adminBody = """
        {
          "email": "SuperAdmin01@gmail.com",
          "password": "SuperAdmin"
        }
        """;

        Response adminLogin = given()
                .header("Content-Type", "application/json")
                .body(adminBody)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .response();

        adminToken = adminLogin.jsonPath().getString("token");

        // Login USER
        String userBody = """
        {
          "email": "User01@gmail.com",
          "password": "User"
        }
        """;

        Response userLogin = given()
                .header("Content-Type", "application/json")
                .body(userBody)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .response();

        userToken = userLogin.jsonPath().getString("token");
    }

    @Test
    @Order(1)
    public void GetAllMedia() {
        Response response =
                given()
                        .header("Authorization", "Bearer " + userToken)
                        .when()
                        .get("/media")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        assertThat("La lista de media no debe ser nula.",
                response.jsonPath().getList("$"), notNullValue());
    }

    @Test
    @Order(2)
    public void CreateMedia() {

        String mediaBody = """
        {
            "title": "Video de prueba",
            "description": "Descripción automática",
            "typeMedia": "Video",
            "url": "https://youtube.com/test",
            "publishDate": "2025-01-01T00:00:00.000+00:00"
        }
        """;

        Response response =
                given()
                        .header("Authorization", "Bearer " + adminToken)
                        .header("Content-Type", "application/json")
                        .body(mediaBody)
                        .when()
                        .post("/media")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        createdMediaId = response.jsonPath().getLong("mediaId");

        assertThat("El mediaId no debería ser nulo", createdMediaId, notNullValue());
        assertThat("El título es incorrecto",
                response.jsonPath().getString("title"), equalTo("Video de prueba"));
    }

    @Test
    @Order(3)
    public void UpdateMedia() {

        String updateBody = """
        {
            "title": "Video actualizado",
            "description": "Descripción actualizada",
            "typeMedia": "Audio",
            "url": "https://spotify.com/test",
            "publishDate": "2026-01-01T00:00:00.000+00:00"
        }
        """;

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + adminToken)
                        .body(updateBody)
                        .when()
                        .put("/media/" + createdMediaId)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        assertThat(response.jsonPath().getString("title"),
                equalTo("Video actualizado"));
        assertThat(response.jsonPath().getString("typeMedia"),
                equalTo("Audio"));
    }

    @Test
    @Order(4)
    public void DeleteMedia() {

        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .delete("/media/" + createdMediaId)
                .then()
                .statusCode(200);

        // Intentar obtenerlo para confirmar que no exista más
        Response response =
                given()
                        .header("Authorization", "Bearer " + adminToken)
                        .when()
                        .get("/media/" + createdMediaId)
                        .then()
                        .extract()
                        .response();

        assertThat("El Media debería haber sido eliminado",
                response.statusCode(), anyOf(is(404), is(500)));
    }

    // ----------- TESTS NEGATIVOS --------------

    @Test
    @Order(5)
    public void CreateMedia_AsUser_Fail() {

        String mediaBody = """
        {
            "title": "No debería crearse",
            "description": "User no tiene permisos",
            "typeMedia": "Video",
            "url": "http://example.com",
            "publishDate": "2024-01-01T00:00:00.000+00:00"
        }
        """;

        given()
                .header("Authorization", "Bearer " + userToken)
                .header("Content-Type", "application/json")
                .body(mediaBody)
                .when()
                .post("/media")
                .then()
                .statusCode(403); // USER no tiene permisos
    }

    @Test

    @Order(6)
    public void DeleteMedia_WithoutToken_Fail() {

        given()
                .when()
                .delete("/media/99999")
                .then()
                .statusCode(403); // Forbidden sin token
    }
    @Test

    @Order(7)
    public void DeleteMedia_AsUser_Fail() {

        given()
                .header("Authorization", "Bearer " + userToken)
                .when()
                .delete("/media/" + createdMediaId) // intenta borrar el último creado
                .then()
                .statusCode(403); // USER no tiene permisos
    }

    @Test
    @Order(8)
    public void UpdateMedia_AsUser_Fail() {

        String updateBody = """
    {
        "title": "Intento de user",
        "description": "USER actualizando contenido",
        "typeMedia": "Video",
        "url": "http://example.com",
        "publishDate": "2026-01-01T00:00:00.000+00:00"
    }
    """;

        given()
                .header("Authorization", "Bearer " + userToken)
                .header("Content-Type", "application/json")
                .body(updateBody)
                .when()
                .put("/media/" + createdMediaId)
                .then()
                .statusCode(403);   // Si devuelve 200 → EL TEST FALLA (No tiene sentido create validado solo admins y update no)
    }


}
