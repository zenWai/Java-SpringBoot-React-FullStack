package com.presa.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

//API Layer
@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /*
    @RequestMapping(
            path = "api/v1/customer",
            method = RequestMethod.GET
    )*/
    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{customerId}")
    public Customer getCustomer(
            @PathVariable("customerId") Integer customerId) {
        return customerService.getCustomer(customerId);
    }
    @PostMapping
    public void registerCustomer(
            @RequestBody CustomerRegistrationRequest request
    ) {
        customerService.addCustomer(request);
    }
    @PutMapping("{customerId}")
    public void updateCustomer(
            @PathVariable("customerId") Integer customerId,
            @RequestBody CustomerUpdateRequest updateRequest
    ){
        customerService.updateCustomer(customerId, updateRequest);
    }
    @DeleteMapping("{customerId}")
    public void deleteCustomerById(
            @PathVariable("customerId") Integer customerId
    ) {
        customerService.deleteCustomerById(customerId);
    }
}
