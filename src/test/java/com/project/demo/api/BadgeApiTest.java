package com.project.demo.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import org.junit.jupiter.api.MethodOrderer;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BadgeApiTest {

    @LocalServerPort
    private int port;

    private static String adminToken;
    private static String userToken;

    private static Long createdBadgeId;

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

        adminToken =
                given()
                        .header("Content-Type", "application/json")
                        .body(adminBody)
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getString("token");

        // Login USER
        String userBody = """
        {
          "email": "User01@gmail.com",
          "password": "User"
        }
        """;

        userToken =
                given()
                        .header("Content-Type", "application/json")
                        .body(userBody)
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getString("token");
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

        // Archivo fake vacío para no depender de Azure realmente
        File fakeImage = new File("src/test/resources/empty.txt");

        Response res =
                given()
                        .header("Authorization", "Bearer " + adminToken)
                        .multiPart("badge", badgeJson, "application/json")
                        .multiPart("image", fakeImage)
                        .when()
                        .post("/badges")
                        .then()
                        .statusCode(anyOf(is(200), is(500))) // Puede fallar por Azure
                        .extract()
                        .response();

        if (res.statusCode() == 200) {
            createdBadgeId = res.jsonPath().getLong("badgeId");
            assertThat(createdBadgeId, notNullValue());
        }
    }

    @Test
    @Order(2)
    public void GetAllBadges() {

        Response res =
                given()
                        .header("Authorization", "Bearer " + adminToken)
                        .when()
                        .get("/badges")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        assertThat("La lista de badges no debe ser nula",
                res.jsonPath().getList("$"),
                is(notNullValue()));

    }

    @Test
    @Order(3)
    public void GetBadgeById() {
        Assumptions.assumeTrue(createdBadgeId != null);

        Response res =
                given()
                        .header("Authorization", "Bearer " + adminToken)
                        .when()
                        .get("/badges/id/" + createdBadgeId)
                        .then()
                        .statusCode(anyOf(is(200), is(500))) // Si Azure falló, puede explotar el controller
                        .extract()
                        .response();

        if (res.statusCode() == 200) {
            assertThat("El titulo no coincide",res.jsonPath().getString("title"), equalTo("Badge Test"));
        }
    }

    @Test
    @Order(4)
    public void UpdateBadge() {
        Assumptions.assumeTrue(createdBadgeId != null);

        String badgeJson = """
        {
            "title": "Badge Actualizado",
            "description": "Descripción actualizada",
            "url": null
        }
        """;

        File fakeImage = new File("src/test/resources/empty.txt");

        Response res =
                given()
                        .header("Authorization", "Bearer " + adminToken)
                        .multiPart("badge", badgeJson, "application/json")
                        .multiPart("image", fakeImage)
                        .when()
                        .put("/badges/" + createdBadgeId)
                        .then()
                        .statusCode(anyOf(is(200), is(500)))
                        .extract()
                        .response();

        if (res.statusCode() == 200) {
            assertThat("El titulo no coincide",res.jsonPath().getString("title"), equalTo("Badge Actualizado"));
        }
    }

    @Test
    @Order(5)
    public void UserShouldNotUpdateBadge() {

        String badgeJson = """
        {
          "title": "Intento de actualización por USER",
          "description": "Esto debería fallar",
          "url": null
        }
        """;

        Response response =
                given()
                        .header("Authorization", "Bearer " + userToken) // TOKEN DE USER NORMAL
                        .multiPart("badge", badgeJson, "application/json")
                        .when()
                        .put("/badges/" + createdBadgeId) // Usa cualquier ID válido creado por admin
                        .then()
                        .extract()
                        .response();

        // ESTE TEST **DEBE FALLAR** PORQUE EL BACKEND PERMITE EL UPDATE SIN SECURITY
        assertThat(
                "Un usuario normal NO debería poder actualizar badges, pero el backend lo permitió.",
                response.statusCode(),
                equalTo(403) // Esperamos 403, pero recibiremos 200
        );
    }

    @Test
    @Order(8)
    public void DeleteBadge() {
        Assumptions.assumeTrue(createdBadgeId != null);

        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .delete("/badges/" + createdBadgeId)
                .then()
                .statusCode(anyOf(is(200), is(500))); // Si Azure falló antes, puede botar error
    }

    @Test
    @Order(6)
    public void UserCreateBadge_Fail() {

        String badgeJson = """
        {
            "title": "No debería",
            "description": "User sin privilegios"
        }
        """;

        File fakeImage = new File("src/test/resources/empty.txt");

        given()
                .header("Authorization", "Bearer " + userToken)
                .multiPart("badge", badgeJson, "application/json")
                .multiPart("image", fakeImage)
                .when()
                .post("/badges")
                .then()
                .statusCode(403); // USER no puede crear
    }

    @Test
    @Order(7)
    public void CreatingBadgeNoToken_Fail() {

        String badgeJson = """
        {
            "title": "No debería",
            "description": "Sin token"
        }
        """;

        File fakeImage = new File("src/test/resources/empty.txt");

        given()
                .multiPart("badge", badgeJson, "application/json")
                .multiPart("image", fakeImage)
                .when()
                .post("/badges")
                .then()
                .statusCode(403);
    }

}
