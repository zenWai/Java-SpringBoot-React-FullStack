package com.presa.customer;

import com.presa.AbstractTestContainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainersUnitTest {
    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest=new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCostumers() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);

        // When
        List<Customer> acualCustomers = underTest.selectAllCostumers();
        //Then
        assertThat(acualCustomers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCostumers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();
        //when
        Optional<Customer> actualCustomer = underTest.selectCustomerById(id);
        //THEN
        assertThat(actualCustomer).isPresent().hasValueSatisfying(
                c-> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                }
        );
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        //given
        int id=-1;
        //when
        Optional<Customer> actualCustomer = underTest.selectCustomerById(id);

        //then
        assertThat(actualCustomer).isEmpty();
    }

    @Test
    void insertCustomer() {
        //given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        //when
        underTest.insertCustomer(customer);

        Customer actualCustomer = underTest.selectAllCostumers()
                .stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(c -> customer)
                .findFirst()
                .orElseThrow();
        //then
        assertThat(actualCustomer).isEqualTo(customer);
    }

    @Test
    void existsPersonWithEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);

        //when
        boolean actualCustomer = underTest.existsPersonWithEmail(email);

        //then

        assertThat(actualCustomer).isTrue();
    }
    @Test
    void existsPersonWithEmailReturnsFalseWhenNotExist() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //when
        boolean actualCustomer = underTest.existsPersonWithEmail(email);

        //then

        assertThat(actualCustomer).isFalse();
    }


    @Test
    void existsPersonWithID() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCostumers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        //when
        boolean existsPersonWithID = underTest.existsPersonWithID(id);
        //then
        assertThat(existsPersonWithID).isTrue();
    }
    @Test
    void existsPersonWithIDWillReturnFalseWhenIdNotPresent() {
        int id=-1;

        //when
        boolean existsPersonWithID = underTest.existsPersonWithID(id);
        //then
        assertThat(existsPersonWithID).isFalse();
    }

    @Test
    void deleteCustomerById() {
        //given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCostumers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();
        //when
        underTest.deleteCustomerById(id);
        //then
        Optional<Customer> actualCustomer = underTest.selectCustomerById(id);
        assertThat(actualCustomer).isNotPresent();
    }

    @Test
    void updateCustomerName() {
        //given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCostumers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        var newName = "foo";

        //when update to newName
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

        //then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName); //updated
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });
    }
    @Test
    void updateCustomerAge() {
        //given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCostumers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        var newAge = 100;

        //when update to newAge
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        //then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(newAge); //updated
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });
    }

    @Test
    void updateCustomerEmail() {
        //given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCostumers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //when update to newEmail
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateCustomer(update);

        //then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getEmail()).isEqualTo(newEmail); //updated
        });
    }

    @Test
    void updateAllCustomerAttributes() {
        //given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCostumers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        var newEmail = "tryingUpdate@fakerino.co";
        var newAge = 100;
        var newName = "Mr fake Test Update";

        //when update to newName
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);
        update.setAge(newAge);
        update.setName(newName);

        underTest.updateCustomer(update);

        //then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName); //updated
            assertThat(c.getAge()).isEqualTo(newAge);  //updated
            assertThat(c.getEmail()).isEqualTo(newEmail); //updated
            assertThat(c.getGender()).isEqualTo(Gender.MALE);
        });
    }
}