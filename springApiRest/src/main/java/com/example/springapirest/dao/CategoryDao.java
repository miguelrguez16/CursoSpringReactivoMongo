package com.example.springapirest.dao;


import com.example.springapirest.documents.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CategoryDao extends ReactiveMongoRepository<Category, String> {
    Mono<Category> findByNameContainsIgnoreCase(String name);


}
