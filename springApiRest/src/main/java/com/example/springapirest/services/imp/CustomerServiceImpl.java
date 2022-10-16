package com.example.springapirest.services.imp;

import com.example.springapirest.dao.CustomerDao;
import com.example.springapirest.documents.Customer;
import com.example.springapirest.services.com.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public Flux<Customer> findAllCustomers() {
        return customerDao.findAll();
    }

    @Override
    public Mono<Customer> findCustomerById(String id) {
        return customerDao.findById(id);
    }

    @Override
    public Mono<Customer> saveCustomer(Customer customer) {
        return customerDao.save(customer);
    }

    @Override
    public Mono<Void> deleteCustomer(Customer customer) {
        return customerDao.delete(customer);
    }
}
