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

import java.util.Random;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }
    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        //customerRepository.
        //findAll(): returns all records of an entity
        //findById(id): returns a record by its primary key
        //save(entity): saves a new record or updates an existing record
        //deleteById(id): deletes a record by its primary key
        //existsById(id): checks if a record exists by its primary key
        //count(): returns the count of records for an entity
        //findAll(Sort sort): returns all records of an entity, sorted by the specified attribute
        //findAll(Pageable pageable): returns a page of records, according to the specified paging and sorting options
        //findBy[Attribute](value): returns a list of records by the specified attribute value
        //findFirstBy[Attribute](value): returns the first record found by the specified attribute value
        //findTopBy[Attribute](value): returns the first record found by the specified attribute value
        //findBy[Attribute]Containing(value): returns a list of records where the specified attribute contains the specified value
        return args -> {
            var faker = new Faker();
            Random random = new Random();
            Name name = faker.name();
            String firstName = name.firstName();
            String lastName = name.lastName();
            int age = random.nextInt(16, 99);
            Gender gender = age % 2 ==0 ? Gender.MALE : Gender.FEMALE;
            Customer customer = new Customer(
                    firstName +  " " + lastName,
                    firstName.toLowerCase() + "." + lastName.toLowerCase() + "@joyjoi",
                    age,
                    gender);
            customerRepository.save(customer);
        };
    }
}
