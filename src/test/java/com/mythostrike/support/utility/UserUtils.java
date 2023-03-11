package com.mythostrike.support.utility;

import com.mythostrike.controller.message.authentication.UserAuthRequest;
import com.mythostrike.support.TestUser;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class UserUtils {

    private UserUtils() {
    }

    public static TestUser createUser(String username, String password) {
        //login user to get jwt Token
        Response response =
            given()
                .body(new UserAuthRequest(username, password)).
                when()
                .post("/users/login").
                then()
                .statusCode(200).
                extract().response();

        //if user does not exist, register user
        if (response.statusCode() == 401) {
            response =
                given()
                    .body(new UserAuthRequest(username, password)).
                    when()
                    .post("/users/register").
                    then()
                    .statusCode(201).
                    extract().response();
        }

        String jwtToken = response.jsonPath().getString("jwtToken");
        assertNotNull(jwtToken, "Could not get token from response: " + response.asString());
        return new TestUser(username, jwtToken);
    }
}
