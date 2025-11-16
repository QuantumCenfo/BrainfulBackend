package com.project.demo.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

public class AuthApiTest {

    private static String token;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    public void loginUser() {
        String body = """
        {
          "email": "User01@gmail.com",
          "password": "User"
        }
        """;

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .body(body)
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();


        token = response.jsonPath().getString("token");
        assertThat("El token no debería ser nulo", token, not(isEmptyOrNullString()));
        assertThat("El rol debería de ser USER",response.jsonPath().getString("authUser.role.name"), equalTo("USER"));
    }

    @Test
    public void loginAdmin() {
        String body = """
        {
          "email": "Admin01@gmail.com",
          "password": "Admin"
        }
        """;

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .body(body)
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        token = response.jsonPath().getString("token");
        assertThat("El token no debería ser nulo", token, not(isEmptyOrNullString()));
        assertThat("El rol debería de ser ADMIN", response.jsonPath().getString("authUser.role.name"), equalTo("ADMIN"));
    }

    @Test
    public void loginSuperAdmin() {
        String body = """
        {
          "email": "SuperAdmin01@gmail.com",
          "password": "SuperAdmin"
        }
        """;

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .body(body)
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        token = response.jsonPath().getString("token");
        assertThat("El token no debería ser nulo", token, not(isEmptyOrNullString()));
        assertThat("El rol debería de ser ADMIN", response.jsonPath().getString("authUser.role.name"), equalTo("SUPER_ADMIN"));
    }

    @Test
    public void loginFailed() {
        String body = """
        {
          "email": "User01@gmail.com",
          "password": "contraseña_incorrecta"
        }
        """;

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .body(body)
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(401)
                        .extract()
                        .response();

        assertThat(response.jsonPath().getString("token"), is(nullValue()));

        assertThat(response.jsonPath().getString("description"),
                equalTo("The username or password is incorrect"));

        assertThat(response.jsonPath().getString("detail"),
                equalTo("Bad credentials"));

        assertThat(response.jsonPath().getString("title"),
                equalTo("Unauthorized"));
    }
}
