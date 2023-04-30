package com.presa.customer;

import com.presa.exception.DuplicateResourceException;
import com.presa.exception.RequestValidationException;
import com.presa.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

//extendwith makes it not need to
//get MockitoAnnotations.openMocks and close Mocks afterEach
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock private CustomerDao customerDao;
    @Mock private PasswordEncoder passwordEncoder;
    private CustomerService underTest;
    private final CustomerDTOMapper customerDTOMapper = new CustomerDTOMapper();
    private static final Random RANDOM = new Random();

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao, customerDTOMapper, passwordEncoder);
    }

    @Test
    void getAllCustomers() {
        //when
        underTest.getAllCustomers();
        //then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //given
        int id=100;

        Customer customer = new Customer(
                id,"maria","mata@gmail.com", "password", 5,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerDTO expected = customerDTOMapper.apply(customer);

        //When
        CustomerDTO actual = underTest.getCustomer(id);
        //then
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void throwWhenGetCostumerReturnsEmptyOptional() {
        //given
        int id=100;

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        //When
        //then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "customer with id [%s] not found".formatted(id));

    }

    @Test
    void addCustomer() {
        //given
        String email = "mata@gmail.com";
        //because of the exception we want to continue after it
        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);
        //we need a customerRegistrationRequest
        int age = RANDOM.nextInt(1, 100);
        Gender gender = age % 2 ==0 ? Gender.MALE : Gender.FEMALE;
        CustomerRegistrationRequest request= new CustomerRegistrationRequest(
                "Joca", email, "password", age, gender
        );

        String passwordHash = "¢5554ml;f;lsd";

        when(passwordEncoder.encode(request.password())).thenReturn(passwordHash);
        //when
        underTest.addCustomer(request);
        //then
        //capture the argument that is our CustomerRegistrationRequest request
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        //perform the assertions
        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
        assertThat(capturedCustomer.getGender()).isEqualTo(request.gender());
        assertThat(capturedCustomer.getPassword()).isEqualTo(passwordHash);
    }
    @Test
    void throwWhenAddCostumerReturnsExistsPersonWithEmail() {
        //given
        var email = "germany@stuff.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);
        int age = RANDOM.nextInt(1, 100);
        Gender gender = age % 2 ==0 ? Gender.MALE : Gender.FEMALE;
        CustomerRegistrationRequest request= new CustomerRegistrationRequest(
                "Joca", email, "password", age, gender
        );
        //When
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");
        //then
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void canUpdateAllCustomerProperties() {
        //given
        int id=100;

        Customer customer = new Customer(
                id,"maria","mata@gmail.com", "password", 5,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "jol@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "jolly", newEmail, 24
        );

        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);


        //when
        underTest.updateCustomer(id, updateRequest);
        //then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        //given
        int id=100;

        Customer customer = new Customer(
                id,"maria","mata@gmail.com", "password", 5,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "jolly", null, null
        );

        //when
        underTest.updateCustomer(id, updateRequest);
        //then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        //given
        int id=100;

        Customer customer = new Customer(
                id,"maria","mata@gmail.com", "password", 5,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "jol@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, newEmail, null
        );

        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);


        //when
        underTest.updateCustomer(id, updateRequest);
        //then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        //given
        int id=100;

        Customer customer = new Customer(
                id,"maria","mata@gmail.com", "password", 5,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, null, 24
        );

        //when
        underTest.updateCustomer(id, updateRequest);
        //then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void ThrowOnCustomerUpdateAndEmailExists() {
        //given
        int id=100;

        Customer customer = new Customer(
                id,"maria","mata@gmail.com", "password", 5,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "jol@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, newEmail, null
        );

        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(true);


        //when
        assertThatThrownBy(()->underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");
        //then

        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void ThrowOnCustomerUpdateWhenNoChanges() {
        //given
        int id=100;

        Customer customer = new Customer(
                id,"maria","mata@gmail.com", "password", 5,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(),customer.getEmail(), customer.getAge()
        );

        //when
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");
        //then
        verify(customerDao, never()).updateCustomer(any());
    }



    @Test
    void deleteCustomerById() {
        //given
        var id = 10;
        when(customerDao.existsPersonWithID(id)).thenReturn(true);

        //when
        underTest.deleteCustomerById(id);
        //then
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void throwWhenDeleteCustomerByIdNotExists() {
        //given
        var id = 10;
        when(customerDao.existsPersonWithID(id)).thenReturn(false);

        //when
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [" + id + "] not found");
        //then
        verify(customerDao,never()).deleteCustomerById(id);
    }
}