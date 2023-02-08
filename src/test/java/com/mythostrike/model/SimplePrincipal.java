package com.mythostrike.model;

import java.security.Principal;

public class SimplePrincipal implements Principal {
    private final String name;

    public SimplePrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
