package com.example.springApiRest.controllers;

import com.example.springApiRest.SpringApiRestApplication;
import com.example.springApiRest.documents.Product;
import com.example.springApiRest.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping("/listar")
    public Flux<Product> listProduct(){
        return productService.findAllProducts();
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<Product>>> listProductEntity(){
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(productService.findAllProducts())
        );
    }

    @GetMapping("/listar/{id}")
    public Mono<ResponseEntity<Mono<Product>>> viewDetail(@PathVariable String id) {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(productService.findProductById(id))
        );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> viewDetail2(@PathVariable String id) {
        return productService.findProductById(id)
                .map(product ->
                        ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                                .body(product)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
