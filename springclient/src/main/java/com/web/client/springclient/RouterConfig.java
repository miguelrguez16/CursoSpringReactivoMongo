package com.web.client.springclient;

import com.web.client.springclient.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(ProductHandler handler){
        return RouterFunctions.route(RequestPredicates.GET("/api/client/"), handler::list)
                .andRoute(RequestPredicates.GET("/api/client/{id}"), handler::view);
    }
}
