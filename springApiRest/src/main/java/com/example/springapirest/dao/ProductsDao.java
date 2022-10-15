package com.example.springapirest.dao;

import com.example.springapirest.documents.Product;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.Map;


public interface ProductsDao extends ReactiveMongoRepository<Product, String> {

    Mono<Product> findByNameContainsIgnoreCase(String name);

    @Query("{'name' :  ?0}")
    Flux<Product> findProductsByNameLimited(String name);

    @Aggregation({
            "{ $group : { _id:  $name}}"
    })
    Flux<Map<String,String>> getAllfilmNames();

    @Aggregation({
            "{ $group : { _id: \"$category.name\" ,count:  {$sum: 1}}}"
    })
    Flux<Map<String,Object>> getTotalProductsByCategory();
}

//"{ $group : { _id : $skills, names : { $push : $name } } }"