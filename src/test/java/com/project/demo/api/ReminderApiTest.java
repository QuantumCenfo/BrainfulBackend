package com.project.demo.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(OrderAnnotation.class)
public class ReminderApiTest {

    private static String token;
    private static Long userId;
    private static Long reminderIdForUpdate;
    private static Long reminderIdForDelete;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";

        String body = """
        {
          "email": "SuperAdmin01@gmail.com",
          "password": "SuperAdmin"
        }
        """;

        Response loginResponse = given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .response();

        token = loginResponse.jsonPath().getString("token");
        userId = loginResponse.jsonPath().getLong("authUser.id");
    }

    @Test
    @Order(1)
    public void CreateReminder() {

        String reminderBody = """
        {
          "reminderDate": "2030-12-31T00:00:00.000+00:00",
          "reminderType": "Daily",
          "name": "Recordatorio automático",
          "reminderDetails": "Prueba de test",
          "user": {
            "id": %d
          }
        }
        """.formatted(userId);

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .body(reminderBody)
                        .when()
                        .post("/api/reminders")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        reminderIdForUpdate = response.jsonPath().getLong("reminderId");
        reminderIdForDelete = reminderIdForUpdate;

        assertThat("El reminderId no debería ser nulo",
                reminderIdForUpdate, notNullValue());
    }

    @Test
    @Order(2)
    public void GetRemindersByUser() {

        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/api/reminders/user/" + userId)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        assertThat("La respuesta no debe ser nula",
                response.jsonPath().getList("$"), is(notNullValue()));
    }

    @Test
    @Order(3)
    public void UpdateReminder() {

        String updatedBody = """
        {
          "reminderDate": "2031-01-01T00:00:00.000+00:00",
          "reminderType": "Weekly",
          "name": "Recordatorio actualizado",
          "reminderDetails": "Detalles nuevos",
          "user": {
            "id": %d
          }
        }
        """.formatted(userId);

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .body(updatedBody)
                        .when()
                        .put("/api/reminders/" + reminderIdForUpdate)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        assertThat(response.jsonPath().getString("name"),
                equalTo("Recordatorio actualizado"));
    }

    @Test
    @Order(4)
    public void GetUpcomingReminders() {

        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/api/reminders/upcoming")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        assertThat("La lista no debe ser nula",
                response.jsonPath().getList("$"), is(notNullValue()));
    }

    @Test
    @Order(5)
    public void DeleteReminder() {

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/api/reminders/" + reminderIdForDelete)
                .then()
                .statusCode(200);

        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/api/reminders/user/" + userId)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        var ids = response.jsonPath().getList("reminderId", Long.class);

        assertThat("El recordatorio aún aparece luego de ser borrado.",
                ids, not(hasItem(reminderIdForDelete)));
    }


    // ----------- TESTS NEGATIVOS --------------

    @Test
    @Order(6)
    public void CreateReminderWithoutUser() {

        String badBody = """
        {
          "reminderDate": "2035-01-01T00:00:00.000+00:00",
          "reminderType": "Daily",
          "name": "Test sin usuario",
          "reminderDetails": "Debe fallar"
        }
        """;

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .body(badBody)
                        .when()
                        .post("/api/reminders")
                        .then()
                        .extract()
                        .response();

        assertThat(response.statusCode(),
                anyOf(is(400), is(500)));
    }

    @Test
    @Order(7)
    public void UpdateReminder_Fail() {

        String updateBody = """
        {
          "reminderDate": "2031-01-01T00:00:00.000+00:00",
          "reminderType": "Daily",
          "name": "Update inexistente",
          "reminderDetails": "Debe fallar",
          "user": {
            "id": %d
          }
        }
        """.formatted(userId);

        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .body(updateBody)
                        .when()
                        .put("/api/reminders/99999999")
                        .then()
                        .extract()
                        .response();

        assertThat(response.statusCode(), anyOf(is(400), is(500)));
    }

    @Test
    @Order(8)
    public void DeleteReminder_Fail() {

        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .delete("/api/reminders/99999999")
                        .then()
                        .extract()
                        .response();

        assertThat(response.statusCode(), anyOf(is(400), is(500)));
    }
}
