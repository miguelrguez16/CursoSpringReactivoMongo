package com.spring.reactive.spring.webflux.services;

import com.spring.reactive.spring.webflux.models.documents.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Flux<Product> findAll();

    Flux<Product> findAllWithNameUpperCase();

    Mono<Product> findById(String id);

    Mono<Product> save(Product product);

    Mono<Void> delete(Product product);

}
