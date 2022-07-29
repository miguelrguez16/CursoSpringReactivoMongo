package com.spring.reactive.spring.webflux.models.dao;

import com.spring.reactive.spring.webflux.models.documents.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductsDao extends ReactiveMongoRepository<Product, String> {

}

