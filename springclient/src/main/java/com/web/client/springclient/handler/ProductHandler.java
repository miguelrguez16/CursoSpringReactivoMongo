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


}
