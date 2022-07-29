package com.spring.reactive.spring.webflux.services;

import com.spring.reactive.spring.webflux.models.documents.Category;
import com.spring.reactive.spring.webflux.models.documents.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Flux<Product> findAllProducts();

    Flux<Category> findAllCategories();

    Flux<Product> findAllProductWithNameUpperCase();

    Mono<Product> findProductById(String id);

    Mono<Product> saveProduct(Product product);

    Mono<Void> deleteProduct(Product product);

    Mono<Category> findCategoryById(String id);


    Mono<Category> saveCategory(Category category);

    Mono<Void> deleteCategory(Category category);


}
