package com.presa.customer;

import com.presa.AbstractTestContainersUnitTest;
import com.presa.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

//datajpatest loads too many stuff
//tests are slow, but for JPA unit testing is hard
//to do without it
@DataJpaTest
//to test with docker off
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestConfig.class})
class CustomerRepositoryTest extends AbstractTestContainersUnitTest {

    @Autowired
    private CustomerRepository underTest;
    //lets check how many beans with this
    @Autowired
    private ApplicationContext applicationContext;
    @BeforeEach
    void setUp() {
        //on our main we create a random Customer every launch
        //to play around
        underTest.deleteAll();
        //getting how many beans
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password",
                20,
                Gender.MALE);
        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();
        //when
        var actualCustomer = underTest.existsCustomerByEmail(email);
        //THEN
        assertThat(actualCustomer).isTrue();

    }
    @Test
    void existsCustomerByEmailFailsWhenNotPresent() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //when
        var actualCustomer = underTest.existsCustomerByEmail(email);
        //THEN
        assertThat(actualCustomer).isFalse();
    }

    @Test
    void existsCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password",
                20,
                Gender.MALE);
        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();
        //when
        var actualCustomer = underTest.existsCustomerById(id);
        //THEN
        assertThat(actualCustomer).isTrue();

    }

    @Test
    void existsCustomerByIdFailsWhenIdNotPresent() {
        int id = -1;
        //when
        var actualCustomer = underTest.existsCustomerById(id);
        //THEN
        assertThat(actualCustomer).isFalse();

    }
}