package com.web.client.springclient.service;

import com.web.client.springclient.dto.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Flux<Product> findAllProducts();
    Mono<Product> findProductById(String id);
    Mono<Product> saveProduct(Product product);
    Mono<Product> updateProduct(Product product, String id);
    Mono<Void> deleteProduct(String id);

}
