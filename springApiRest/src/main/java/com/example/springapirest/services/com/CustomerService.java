package com.example.springapirest.services.com;

import com.example.springapirest.documents.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<Customer> findAllCustomers();

    Mono<Customer> findCustomerById(String id);

    Mono<Customer> saveCustomer(Customer customer);

    Mono<Void> deleteCustomer(Customer customer);
}
