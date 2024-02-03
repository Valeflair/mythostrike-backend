package com.mythostrike.integration;

import com.mythostrike.controller.message.authentication.ChangeAvatarRequest;
import com.mythostrike.controller.message.authentication.UserAuthRequest;
import com.mythostrike.controller.message.lobby.LobbyIdRequest;
import com.mythostrike.support.TestUser;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
public class RessourcesIntegrationTest {

    private static final Integer PORT = 8080;

    @BeforeAll
    static void restAssuredSetup() {
        RestAssured.requestSpecification = new RequestSpecBuilder().build()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON);
        RestAssured.port = PORT;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void getChampionsTest() {
        given().
            when()
            .get("/resources/champions").
            then()
            .statusCode(200)
            .body(notNullValue());
    }

    @Test
    void getCardsTest() {
        given().
            when()
            .get("/resources/cards").
            then()
            .statusCode(200)
            .body(notNullValue());
    }

    @Test
    void getModesTest() {
        given().
            when()
            .get("/resources/modes").
            then()
            .statusCode(200)
            .body(notNullValue());
    }

    @Test
    void getLobbiesTest() {
        given().
            when()
            .get("/lobbies").
            then()
            .statusCode(200)
            .body(notNullValue());
    }

    @Test
    void getRankListTest() {
        given().
            when()
            .get("/users/ranklist").
            then()
            .statusCode(200)
            .body(notNullValue());
    }

    @Test
    void allUserRequests() {
        String username = RandomStringUtils.random(20, true, true);
        String password = RandomStringUtils.random(20, true, true);

        //try login not registered user
        given()
            .body(new UserAuthRequest(username, password)).
            when()
            .post("/users/login").
            then()
            .statusCode(401);

        //register user
        given()
            .body(new UserAuthRequest(username, password)).
            when()
            .post("/users/register").
            then()
            .statusCode(201);

        //try register user again
        given()
            .body(new UserAuthRequest(username, password)).
            when()
            .post("/users/register").
            then()
            .statusCode(409);

        //login wrong password
        given()
            .body(new UserAuthRequest(username, password + "l")).
            when()
            .post("/users/login").
            then()
            .statusCode(401);

        //login correct password
        String jwtToken =
            given()
                .body(new UserAuthRequest(username, password)).
                when()
                .post("/users/login").
                then()
                .statusCode(200)
                .body("jwtToken", notNullValue()).
                extract().body().jsonPath().getString("jwtToken");

        TestUser user = new TestUser(username, jwtToken);

        //get user data
        given()
            .headers(user.headers()).
            when()
            .post("/users/data").
            then()
            .statusCode(200)
            .body(notNullValue());

        //change avatar
        given()
            .headers(user.headers())
            .body(new ChangeAvatarRequest(2)).
            when()
            .post("/users/avatar").
            then()
            .statusCode(200);

    }
}
