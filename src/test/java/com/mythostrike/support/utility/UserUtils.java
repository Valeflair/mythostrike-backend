package com.mythostrike.support.utility;

import com.mythostrike.controller.message.authentication.UserAuthRequest;
import com.mythostrike.support.TestUser;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class UserUtils {
    public static final int I_TEST_USER = 0;
    public static final int I_REINER_ZUFALL = 1;
    public static final int I_MINH = 2;
    public static final int I_JACK = 3;
    public static final int I_TILL = 4;
    public static final int I_HONG = 5;
    public static final int I_LAITO = 6;
    public static final int I_MAX_MUSTERMAN = 7;
    private final List<TestUser> users = new ArrayList<>();
    private static UserUtils instance = null;

    private UserUtils() {
        users.add(createUser("TestUser", "TestPassword"));
        assertFalse(users.get(I_TEST_USER).jwtToken().isEmpty());

        users.add(createUser("Reiner Zufall", "12341234"));
        assertFalse(users.get(I_REINER_ZUFALL).jwtToken().isEmpty());

        users.add(createUser("Minh-Trung Minh-Trung Tang", "MinhTrungTangMinhTrungTang"));
        assertFalse(users.get(I_MINH).jwtToken().isEmpty());

        users.add(createUser("__Jack__", "JackyChanJackyChan"));
        assertFalse(users.get(I_JACK).jwtToken().isEmpty());

        users.add(createUser("Till1234", "IchBinEinCoolesPassword"));
        assertFalse(users.get(I_TILL).jwtToken().isEmpty());

        users.add(createUser("Hong Big Kong", "StärkerAlsDerStärkste"));
        assertFalse(users.get(I_HONG).jwtToken().isEmpty());

        users.add(createUser("Laito_oXo", "=^_^="));
        assertFalse(users.get(I_LAITO).jwtToken().isEmpty());

        users.add(createUser("Max Musterman", "DieLegendeDerTestLegenden"));
        assertFalse(users.get(I_MAX_MUSTERMAN).jwtToken().isEmpty());
    }

    public static UserUtils getInstance() {
        if(instance == null) {
            instance = new UserUtils();
        }
        return instance;
    }

    private static TestUser createUser(String username, String password) {
        //login user to get jwt Token
        Response response =
            given()
                .body(new UserAuthRequest(username, password)).
                when()
                .post("/users/login").
                then().
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
        } else {
            assertEquals(200, response.statusCode(), "Could not login user: " + response.asString());
        }

        String jwtToken = response.jsonPath().getString("jwtToken");
        assertNotNull(jwtToken, "Could not get token from response: " + response.asString());
        return new TestUser(username, jwtToken);
    }

    public List<TestUser> getUsers(int numberOfUsers) {
        return users.subList(0, numberOfUsers);
    }
}
