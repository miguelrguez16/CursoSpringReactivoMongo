package com.example.springapirest.handler;

import com.example.springapirest.documents.Product;
import com.example.springapirest.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;

@Component
public class ProductHandler {
    @Autowired
    private ProductService productService;

    /**
     * @param serverRequest request fr
     * @return list of products
     */
    public Mono<ServerResponse> list(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.findAllProducts(), Product.class);
    }

    /**
     * @param serverRequest with the id of the product to see
     * @return
     */
    public Mono<ServerResponse> detail(ServerRequest serverRequest) {
        String idRequest = serverRequest.pathVariable("id");
        return productService.findProductById(idRequest)
                .flatMap(product -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(product)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Save a product from a post
     *
     * @param serverRequest with the product in Json format
     * @return the uri to find the product and the product
     */
    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        Mono<Product> productMono = serverRequest.bodyToMono(Product.class);
        return productMono.flatMap(product -> {
                    if (product.getCreateAt() == null) product.setCreateAt(new Date());
                    return productService.saveProduct(product);
                })
                .flatMap(product ->
                        ServerResponse.created(URI.create("/api/v2/products/".concat(product.getId())))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(product)));
    }

    public Mono<ServerResponse> edit(ServerRequest serverRequest) {
        String idRequest = serverRequest.pathVariable("id");
        Mono<Product> productMono = serverRequest.bodyToMono(Product.class);

        Mono<Product> productMonoDb = productService.findProductById(idRequest);

        return productMonoDb.zipWith(productMono, (db, req) -> {
            db.setName(req.getName())
                    .setPrice(req.getPrice())
                    .setCategory(req.getCategory());
            return db;
        }).flatMap(product -> ServerResponse.created(URI.create("/api/v2/products/".concat(product.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(product)));
    }

}
