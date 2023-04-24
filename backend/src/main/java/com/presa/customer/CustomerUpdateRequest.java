package com.presa.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
