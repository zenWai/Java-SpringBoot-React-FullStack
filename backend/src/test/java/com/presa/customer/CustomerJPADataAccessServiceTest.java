package com.presa.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        Page<Customer> page = mock(Page.class);
        List<Customer> customers = List.of(new Customer());
        when(page.getContent()).thenReturn(customers);
        when(customerRepository.findAll(any(Pageable.class))).thenReturn(page);
        // When
        List<Customer> expected = underTest.selectAllCustomers();

        // Then
        assertThat(expected).isEqualTo(customers);
        ArgumentCaptor<Pageable> pageArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(customerRepository).findAll(pageArgumentCaptor.capture());
        assertThat(pageArgumentCaptor.getValue()).isEqualTo(Pageable.ofSize(180));
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
                1,"John","joca@gmail.com", "password", 2,
                Gender.MALE);
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
        underTest.existsCustomerWithEmail(email);
        //then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existsPersonWithID() {
        //given
        var id = 100;
        //when
        underTest.existsCustomerWithID(id);
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
                5,"Jy","joly@gmail.com", "password", 2,
                Gender.MALE);
        //when
        underTest.updateCustomer(customer);
        //then
        verify(customerRepository).save(customer);
    }

    @Test
    void canUpdateProfileImageId() {
        // Given
        String profileImageId = "2222";
        Integer customerId = 1;

        // When
        underTest.updateCustomerProfileImageId(profileImageId, customerId);

        // Then
        verify(customerRepository).updateProfileImageId(profileImageId, customerId);
    }

}