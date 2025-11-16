package com.project.demo.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ForumApiTest {

    private static String token;
    private static Long userId;
    private static Long forumIdForComment;
    private static Long forumIdForDelete;
    private static Long commentIdForDelete;

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
    public void GetAllForums() {
        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/forums")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        assertThat("La lista no debe ser nula",
                response.jsonPath().getList("$"), is(notNullValue()));
    }

    @Test
    @Order(2)
    public void CreateForum() {
        String foroBody = """
        {
          "title": "Foro creado por test",
          "description": "Descripción de pruebas",
          "anonymous": false,
          "user": {
            "id": %d
          }
        }
        """.formatted(userId); // 👈 inserta dinámicamente el id real

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(foroBody)
                .when()
                .post("/forums")
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertThat(response.jsonPath().getLong("user.id"), equalTo(userId));
        assertThat(response.jsonPath().getString("title"), equalTo("Foro creado por test"));
        forumIdForComment = response.jsonPath().getLong("forumId");
    }

    @Test
    @Order(3)
    public void GetForumsFromUser() {
        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/forums/UserId/" + userId)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();


        assertThat("La respuesta de /forums/UserId/" + userId + " no contiene foros.", response.jsonPath().getList("$").size(), greaterThan(0));
        // Validación adicional: todos los foros deben pertenecer al usuario logueado
        forumIdForDelete = response.jsonPath().getLong("[0].forumId");
        var userIds = response.jsonPath().getList("user.id", Integer.class);
        assertThat("Se encontró un foro cuyo user.id no coincide con el userId logueado (" + userId + ").", userIds, everyItem(equalTo(userId.intValue())));
    }

    @Test
    @Order(4)
    public void DeleteForumById() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/forums/" + forumIdForDelete)
                .then()
                .statusCode(200); // el delete del backend no devuelve nada, pero sí debe retornar 200

        Response getResponse =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/forums/" + forumIdForDelete)
                        .then()
                        .extract()
                        .response();

        // Debe dar un 500 o 404
        assertThat(
                "El foro con ID " + forumIdForDelete + " debería haber sido eliminado.",
                getResponse.statusCode(),
                is(anyOf(is(500), is(404)))
        );
    }

    @Test
    @Order(5)
    public void CreateComment() {
        String commentBody = """
        {
          "content": "Comentario de prueba automático",
          "anonymous": true,
          "user": {
            "id": %d
          },
          "forum": {
            "forumId": %d
          }
        }
        """.formatted(userId, forumIdForComment);

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .body(commentBody)
                        .when()
                        .post("/comments")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        Long commentId = response.jsonPath().getLong("commentId");
        assertThat("El commentId no debería ser nulo", commentId, notNullValue());
        String content = response.jsonPath().getString("content");
        assertThat("El contenido no coincide",
                content, equalTo("Comentario de prueba automático"));
        Long returnedForumId = response.jsonPath().getLong("forum.forumId");
        assertThat("El foro asociado es incorrecto",
                returnedForumId, equalTo(forumIdForComment));
        Long returnedUserId = response.jsonPath().getLong("user.id");
        assertThat("El comentario debería pertenecer al usuario autenticado",
                returnedUserId, equalTo(userId));
        Boolean anonymous = response.jsonPath().getBoolean("anonymous");
        assertThat("El comentario debería ser anónimo", anonymous, is(true));
        commentIdForDelete = response.jsonPath().getLong("commentId");
    }

    @Test
    @Order(6)
    public void GetCommentsFromForum() {

        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/comments/" + forumIdForComment)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        // Debe haber al menos 1 comentario (el que acabamos de crear)
        var comments = response.jsonPath().getList("$");
        assertThat("No se devolvieron comentarios del foro.", comments.size(), greaterThan(0));
        var ids = response.jsonPath().getList("commentId", Long.class);
        assertThat(
                "El comentario creado (ID " + commentIdForDelete + ") no aparece en la lista.",
                ids,
                hasItem(commentIdForDelete)
        );
    }

    @Test
    @Order(7)
    public void DeleteComment() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/comments/" + commentIdForDelete)
                .then()
                .statusCode(200);

        // GET para asegurar que ya NO existe
        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/comments/" + forumIdForComment)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        var ids = response.jsonPath().getList("commentId", Long.class);

        assertThat(
                "El comentario con ID " + commentIdForDelete + " todavía aparece después de borrarlo.",
                ids,
                not(hasItem(commentIdForDelete))
        );
    }

    @Test
    @Order(8)
    public void CreateForumWithoutTitleFail() {
        String foroBody = """
        {
          "description": "Foro sin título",
          "anonymous": false,
          "user": {
            "id": %d
          }
        }
        """.formatted(userId);

        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json")
                        .body(foroBody)
                        .when()
                        .post("/forums")
                        .then()
                        .extract()
                        .response();

        assertThat(
                "Debería fallar al crear un foro sin título",
                response.statusCode(),
                anyOf(is(400), is(500))
        );
    }

    @Test
    @Order(9)
    public void CreateCommentWithoutContentFail() {

        String commentBody = """
        {
          "anonymous": true,
          "user": {
            "id": %d
          },
          "forum": {
            "forumId": %d
          }
        }
        """.formatted(userId, forumIdForComment);

        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json")
                        .body(commentBody)
                        .when()
                        .post("/comments")
                        .then()
                        .extract()
                        .response();

        assertThat(
                "El comentario sin contenido debería fallar.",
                response.statusCode(),
                anyOf(is(400), is(500))
        );
    }

    @Test
    @Order(10)
    public void CreateCommentWithoutUserFail() {

        String commentBody = """
        {
          "content": "Comentario inválido",
          "anonymous": true,
          "forum": {
            "forumId": %d
          }
        }
        """.formatted(forumIdForComment);

        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json")
                        .body(commentBody)
                        .when()
                        .post("/comments")
                        .then()
                        .extract()
                        .response();

        assertThat(
                "El comentario sin user debería fallar.",
                response.statusCode(),
                anyOf(is(400), is(500))
        );
    }

    @Test
    @Order(11)
    public void CreateCommentWithNonExistingForumFail() {

        String commentBody = """
        {
          "content": "No debería crearse",
          "anonymous": true,
          "user": {
            "id": %d
          },
          "forum": {
            "forumId": 999999
          }
        }
        """.formatted(userId);

        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json")
                        .body(commentBody)
                        .when()
                        .post("/comments")
                        .then()
                        .extract()
                        .response();

        assertThat(
                "Crear comentario con foro inexistente debería fallar.",
                response.statusCode(),
                is(500)
        );
    }

    @Test
    @Order(12)
    public void CreateCommentWithoutTokenFail() {

        String commentBody = """
        {
          "content": "Comentario ilegal",
          "anonymous": false,
          "user": {
            "id": %d
          },
          "forum": {
            "forumId": %d
          }
        }
        """.formatted(userId, forumIdForComment);

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .body(commentBody)
                        .when()
                        .post("/comments")
                        .then()
                        .extract()
                        .response();

        assertThat(
                "Sin token debe devolver 403.",
                response.statusCode(),
                is(403)
        );
    }

}