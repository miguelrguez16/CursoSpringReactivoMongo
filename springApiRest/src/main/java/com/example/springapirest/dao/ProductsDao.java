package com.example.springapirest.dao;

import com.example.springapirest.documents.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductsDao extends ReactiveMongoRepository<Product, String> {

}

