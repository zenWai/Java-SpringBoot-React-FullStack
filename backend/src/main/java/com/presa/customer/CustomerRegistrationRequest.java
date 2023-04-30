package com.presa.customer;

//DTO for registration
public record CustomerRegistrationRequest(
    String name,
    String email,
    String password,
    Integer age,
    Gender gender
)  {

}
