package com.example.springApiRest.controllers;

import com.example.springApiRest.documents.Product;
import com.example.springApiRest.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public Flux<Product> listProduct(){
        return productService.findAllProducts();
    }

    @GetMapping("/listar")
    public Mono<ResponseEntity<Flux<Product>>> listProductEntity(){
        return Mono.just(ResponseEntity.ok(productService.findAllProducts()));
    }
}
