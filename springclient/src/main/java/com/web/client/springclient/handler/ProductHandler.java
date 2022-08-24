package com.web.client.springclient.handler;

import com.web.client.springclient.dto.Product;
import com.web.client.springclient.service.ProductService;
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

    public Mono<ServerResponse> list(ServerRequest serverRequest){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.findAllProducts(), Product.class);

    }

    public Mono<ServerResponse> view(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");
        return productService.findProductById(id)
                .flatMap(product -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(product)))
                .switchIfEmpty(ServerResponse.notFound().build());

    }


    public Mono<ServerResponse> create(ServerRequest serverRequest){
        Mono<Product> productMono = serverRequest.bodyToMono(Product.class);
        return productMono.flatMap(product -> {
            if(product.getCreateAt()==null) product.setCreateAt(new Date());
            return productService.saveProduct(product);
        }).flatMap(product ->
                ServerResponse.created(URI.create("/api/client/".concat(product.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(product))
        );
    }

    public Mono<ServerResponse> edit(ServerRequest serverRequest){
        // TODO review call to service
        Mono<Product> productMono = serverRequest.bodyToMono(Product.class);
        String id = serverRequest.pathVariable("id");

        return productMono.flatMap(product ->
            ServerResponse.created(URI.create("/api/client/".concat(id)))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(productService.updateProduct(product,id), Product.class)
        );
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return productService.deleteProduct(id).then(ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    }
