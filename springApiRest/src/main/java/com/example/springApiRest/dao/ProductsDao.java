package com.example.springApiRest.dao;

import com.example.springApiRest.documents.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductsDao extends ReactiveMongoRepository<Product, String> {

}

