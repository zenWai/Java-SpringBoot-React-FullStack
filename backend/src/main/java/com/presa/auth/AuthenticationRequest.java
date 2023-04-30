package com.presa.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
