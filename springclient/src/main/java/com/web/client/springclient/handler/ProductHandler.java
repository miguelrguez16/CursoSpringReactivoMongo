package com.web.client.springclient.handler;

import com.web.client.springclient.dto.Product;
import com.web.client.springclient.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.swing.*;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class ProductHandler {
    private static final Logger log = LoggerFactory.getLogger(ProductHandler.class);

    @Autowired
    private ProductService productService;

    public Mono<ServerResponse> list(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.findAllProducts(), Product.class);

    }

    public Mono<ServerResponse> view(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return this.errorHandler(
                productService.findProductById(id)
                        .flatMap(product -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(product)))
                        .switchIfEmpty(ServerResponse.notFound().build())
        )
                ;

    }


    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        Mono<Product> productMono = serverRequest.bodyToMono(Product.class);
        return this.errorHandler(
                productMono.flatMap(product -> {
                    if (product.getCreateAt() == null) product.setCreateAt(new Date());
                    return productService.saveProduct(product);
                }).flatMap(product ->
                        ServerResponse.created(URI.create("/api/client/".concat(product.getId())))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(product))
                )
        )
                ;
    }

    public Mono<ServerResponse> edit(ServerRequest serverRequest) {
        Mono<Product> productMono = serverRequest.bodyToMono(Product.class);
        String id = serverRequest.pathVariable("id");

        return this.errorHandler(
                productMono
                        .flatMap(product -> productService.updateProduct(product, id))
                        .flatMap(product ->
                                ServerResponse.created(URI.create("/api/client/".concat(product.getId())))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue(product))
                        )
        )
                ;
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return this.errorHandler(
                productService.deleteProduct(id).then(ServerResponse.noContent().build())
                        .switchIfEmpty(ServerResponse.notFound().build())
            )
                ;
    }

    public Mono<ServerResponse> upload(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return this.errorHandler(
                serverRequest.multipartData()
                        .map(
                                stringMultiValueMap -> stringMultiValueMap.toSingleValueMap()
                                        .get("file")).cast(FilePart.class)
                        .flatMap(file -> productService.uploadImage(file, id))
                        .flatMap(product -> ServerResponse.created(URI.create("/api/client/".concat(id)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(product, Product.class))
        );
    }


    public Mono<ServerResponse> errorHandler(Mono<ServerResponse> serverResponseMono) {
        return serverResponseMono.onErrorResume(error -> {
            WebClientResponseException errorResponse = (WebClientResponseException) error;
            Map<String, String> bodyErrorMap = new HashMap<>();
            bodyErrorMap.put("error", "Product does not exists ".concat(Objects.requireNonNull(errorResponse.getMessage())));
            bodyErrorMap.put("time", new Date().toString());
            bodyErrorMap.put("status", errorResponse.getStatusCode().toString());

            log.error(errorResponse.toString());
            return ServerResponse.status(HttpStatus.NOT_FOUND).body(BodyInserters.fromValue(bodyErrorMap));
        });
    }
}
