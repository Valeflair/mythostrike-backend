package com.mythostrike.support;

import io.restassured.http.Header;
import io.restassured.http.Headers;

import java.util.ArrayList;
import java.util.List;

public record TestUser(String username, int avatarNumber, String jwtToken, Headers headers) {
    public TestUser(String username, String jwtToken) {
        this(username, 1, jwtToken, createHeader(jwtToken));
    }

    private static Headers createHeader(String jwtToken) {
        List<Header> headerList = new ArrayList<>();

        headerList.add(new Header("Authorization", "Bearer " + jwtToken));
        return new Headers(headerList);
    }
}
