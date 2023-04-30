package com.presa.customer;

import com.presa.exception.DuplicateResourceException;
import com.presa.exception.RequestValidationException;
import com.presa.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//Business Layer
@Service
public class CustomerService {
    private final CustomerDao customerDao;
    private final CustomerDTOMapper customerDTOMapper;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao,
                           CustomerDTOMapper customerDTOMapper, PasswordEncoder passwordEncoder) {
        this.customerDao = customerDao;
        this.customerDTOMapper = customerDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerDao.selectAllCustomers()
                .stream()
                .map(customerDTOMapper)
                .collect(Collectors.toList());
    }
    public CustomerDTO getCustomer(Integer id) {
        return customerDao.selectCustomerById(id)
                .map(customerDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "customer with id [%s] not found".formatted(id)
                ));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        String email = customerRegistrationRequest.email();
        if (customerDao.existsPersonWithEmail(email)) {
            throw new DuplicateResourceException(
                    "Email already taken"
            );
        }

        //add
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                passwordEncoder.encode(customerRegistrationRequest.password()),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender());
        customerDao.insertCustomer(customer);
    }
public void updateCustomer(Integer customerId,
                           CustomerUpdateRequest updateRequest) {

    Customer customer = customerDao.selectCustomerById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "customer with id [%s] not found".formatted(customerId)
            ));
        boolean changes = false;

        if( updateRequest.name() != null && !updateRequest.name().equals(customer.getName()) ) {
            customer.setName(updateRequest.name());
            changes = true;
        }

        if( updateRequest.age() != null && !updateRequest.age().equals(customer.getAge()) ) {
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if( updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail()) ) {
            if( customerDao.existsPersonWithEmail(updateRequest.email()) ) {
                throw new DuplicateResourceException(
                        "email already taken"
                );
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if(!changes) {
            throw new RequestValidationException("no data changes found");
        }

        customerDao.updateCustomer(customer);
    }
    public void deleteCustomerById(Integer customerId) {
        if( !customerDao.existsPersonWithID(customerId) ) {
            throw new ResourceNotFoundException(
                    "Customer with id [" + customerId + "] not found"
            );
        }
        customerDao.deleteCustomerById(customerId);
    }
}
