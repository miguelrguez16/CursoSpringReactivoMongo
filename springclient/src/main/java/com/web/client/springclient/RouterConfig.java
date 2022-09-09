package com.web.client.springclient;

import com.web.client.springclient.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class RouterConfig {

    private static final String DEFAULT_ROUTE = "/api/client/";
    private static final String DEFAULT_ROUTE_ID = "/api/client/{id}";


    @Bean
    public RouterFunction<ServerResponse> routes(ProductHandler handler){
        return RouterFunctions.route(RequestPredicates.GET(DEFAULT_ROUTE), handler::list)
                .andRoute(RequestPredicates.GET(DEFAULT_ROUTE_ID), handler::view)
                .andRoute(RequestPredicates.POST(DEFAULT_ROUTE), handler::create)
                .andRoute(RequestPredicates.PUT(DEFAULT_ROUTE_ID), handler::edit)
                .andRoute(RequestPredicates.DELETE(DEFAULT_ROUTE_ID), handler::delete)
                .andRoute(RequestPredicates.POST(DEFAULT_ROUTE_ID), handler::upload)
                ;
    }
}
