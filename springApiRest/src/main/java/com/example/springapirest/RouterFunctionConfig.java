package com.example.springapirest;

import com.example.springapirest.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(ProductHandler productHandler) {
        return RouterFunctions
                .route(GET("/api/v2/products").or(GET("/api/v3/products")),productHandler::list)
                .andRoute(GET("/api/v2/products/{id}"),productHandler::detail);
    }   // indicate response type is JSON with .and(contentType(MediaType.APPLICATION_JSON))
        // the request must have indicated content-type: json
}
