package com.example.springapirest;

import com.example.springapirest.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterFunctionConfig {
    private static final String DEFAULT_URI ="/api/v2/products";

    @Bean
    public RouterFunction<ServerResponse> routes(ProductHandler productHandler) {
        return RouterFunctions
                .route(GET(DEFAULT_URI).or(GET("/api/v3/products")),productHandler::list)
                .andRoute(GET(DEFAULT_URI.concat("/{id}")),productHandler::detail)
                .andRoute(POST(DEFAULT_URI),productHandler::save)
                .andRoute(PUT(DEFAULT_URI),productHandler::edit);

    }   // indicate response type is JSON with .and(contentType(MediaType.APPLICATION_JSON))
        // the request must have indicated content-type: json
}
