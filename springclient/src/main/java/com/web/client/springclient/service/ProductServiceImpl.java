package com.web.client.springclient.service;

import com.web.client.springclient.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private WebClient client;


    /**
     * @return Flux<Product>
     */
    @Override
    public Flux<Product> findAllProducts() {
        return client.get()
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToFlux(clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                        return clientResponse.bodyToFlux(Product.class);
                    } else if (clientResponse.statusCode().is4xxClientError()) {
                        return Flux.error(new RuntimeException("NOT FOUnd error 400 " + clientResponse.toString()));
                    } else {
                        return Flux.error(new RuntimeException("Other error " + clientResponse.toString()));
                    }
                });
    }

    /**
     * @param id of Product to search
     * @return Product looking for
     */
    @Override
    public Mono<Product> findProductById(String id) {
        if (id.isBlank() || id.length() < 25) return Mono.just(new Product().setName("Error"));
        return client.get()
                .uri("/{id}", Collections.singletonMap("id",id))
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(apiResponse -> {
                    if (apiResponse.statusCode().is2xxSuccessful()) return apiResponse.bodyToMono(Product.class);
                    else return Mono.error(new RuntimeException("Not found error " + apiResponse.toString()));
                });
    }

    /**
     * @param product
     * @return Product saved
     */
    @Override
    public Mono<Product> saveProduct(Product product) {

        return client.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(product))
                .exchangeToMono(clientResponse -> {
                    if(clientResponse.statusCode().is2xxSuccessful()) return clientResponse.bodyToMono(Product.class);
                    else return Mono.error( new RuntimeException(""));
                })
                ;
    }

    /**
     * @param product
     * @param id
     * @return
     */
    @Override
    public Mono<Product> updateProduct(Product product, String id) {
        if (id.isBlank() || id.length() < 25) return Mono.just(new Product().setName("Error id: update"));
        return client.put()
                .uri("/{id}", Collections.singletonMap("id",id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(product))
                .exchangeToMono(clientResponse -> {
                    if(clientResponse.statusCode().is2xxSuccessful()) return clientResponse.bodyToMono(Product.class);
                    else return Mono.error( new RuntimeException(""));
                })
                ;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Mono<Void> deleteProduct(String id) {
        if (id.isBlank() || id.length() < 25) return Mono.error(new RuntimeException("Product not found"));
        return client.delete()
                .uri("/{id}", Collections.singletonMap("id",id))
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> Mono.just(clientResponse.headers()))
                .then()
                ;
    }
}
