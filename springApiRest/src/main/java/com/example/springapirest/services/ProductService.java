package com.example.springapirest.services;

import com.example.springapirest.documents.Category;
import com.example.springapirest.documents.Product;
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

    Mono<Product> findProductByNameContainsIgnoreCase(String name);

    Mono<Category> findCategoryByNameContainsIgnoreCase(String name);


}
