package com.spring.reactive.spring.webflux.models.dao;

import com.spring.reactive.spring.webflux.models.documents.Product;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.List;


public interface ProductsDao extends ReactiveMongoRepository<Product, String> {
//    @Query(value ="{'name': ?0}", fields = "{'name':  1}")
//    Flux<Product> findFirstByNameAndCreateAt(String name, Date createAt);
}

