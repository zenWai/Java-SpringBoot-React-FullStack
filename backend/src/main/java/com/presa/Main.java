package com.presa;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.presa.customer.Customer;
import com.presa.customer.CustomerRepository;
import com.presa.customer.Gender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);


    }
    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            createRandomCustomer(customerRepository, passwordEncoder);
        };
    }

    private static void createRandomCustomer(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        var faker = new Faker();
        Random random = new Random();
        Name name = faker.name();

        String firstName = name.firstName();
        String lastName = name.lastName();
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@fakerino.com";
        String password = "password";
        int age = random.nextInt(16, 99);
        Gender gender = age % 2 ==0 ? Gender.MALE : Gender.FEMALE;

        Customer customer = new Customer(
                firstName +  " " + lastName,
                email,
                password,
                age,
                gender);
        customerRepository.save(customer);
        System.out.println(email);
    }

}
