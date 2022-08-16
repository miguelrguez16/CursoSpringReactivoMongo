package com.example.springapirest.dao;

import com.example.springapirest.documents.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ProductsDao extends ReactiveMongoRepository<Product, String> {

    Mono<Product> findByNameContainsIgnoreCase(String name);

//    @Query("{'name' :  ?0}")
//    Mono<Product> obteinProductByName(String name);


}

