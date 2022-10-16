package com.example.springapirest.dao;

import com.example.springapirest.documents.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerDao extends ReactiveMongoRepository<Customer, String> {
}
