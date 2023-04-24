package com.presa.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock private CustomerRepository customerRepository;
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCostumers() {
        //when
        underTest.selectAllCostumers();
        //then
        verify(customerRepository)
                .findAll();
    }

    @Test
    void selectCustomerById() {
        //given
        int id = 1;
        //when
        underTest.selectCustomerById(id);
        //then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        //given
        Customer customer = new Customer(
                1,"John","joca@gmail.com",2
        );
        //when
        underTest.insertCustomer(customer);
        //then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        //given
        var email = "joly@mail.com";
        //when
        underTest.existsPersonWithEmail(email);
        //then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existsPersonWithID() {
        //given
        var id = 100;
        //when
        underTest.existsPersonWithID(id);
        //then
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        //given
        var id = 100;
        //when
        underTest.deleteCustomerById(id);
        //then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        //given
        Customer customer = new Customer(
                5,"Jy","joly@gmail.com",2
        );
        //when
        underTest.updateCustomer(customer);
        //then
        verify(customerRepository).save(customer);
    }
}