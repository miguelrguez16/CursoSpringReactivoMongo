package com.spring.reactive.spring.webflux.controllers;

import com.spring.reactive.spring.webflux.models.dao.ProductsDao;
import com.spring.reactive.spring.webflux.models.documents.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductoRestController {


    @Autowired
    private ProductsDao productsDao;

    private static final Logger log = LoggerFactory.getLogger(ProductoRestController.class);

    @GetMapping
    public Flux<Product> index() {
        return productsDao.findAll()
                .map(product -> product.setName(product.getName().toUpperCase()))
                .doOnNext(product -> log.info(product.toString()));
    }

    @GetMapping("/{id}")
    public Mono<Product> showOneById(@PathVariable String id) {

//        Flux<Product> productFlux = productsDao.findAll();
//        Mono<Product> productMono = productFlux.filter(product -> product.getId().equals(id))
//                .next()
//                .doOnNext(product -> log.info(product.toString()));
//

        return productsDao.findById(id).map(product -> product.setName(product.getName().toUpperCase()))
                .doOnNext(product -> log.info(product.toString()));
    }

}
