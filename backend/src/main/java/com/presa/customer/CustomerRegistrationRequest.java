package com.presa.customer;

public record CustomerRegistrationRequest(
    String name,
    String email,
    Integer age,
    Gender gender
)  {

}
