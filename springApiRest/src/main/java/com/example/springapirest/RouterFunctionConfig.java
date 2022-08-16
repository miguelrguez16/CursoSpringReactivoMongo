package com.example.springapirest;

import com.example.springapirest.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.example.springapirest.utils.Utils.DEFAULT_URI;
import static com.example.springapirest.utils.Utils.DEFAULT_URI_ID;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(ProductHandler productHandler) {
        return RouterFunctions
                .route(GET( DEFAULT_URI).or(GET("/api/v3/products")), productHandler::list)
                .andRoute(GET(DEFAULT_URI_ID), productHandler::detail)
                .andRoute(POST(DEFAULT_URI), productHandler::save)
                .andRoute(PUT(DEFAULT_URI_ID), productHandler::edit)
                .andRoute(DELETE(DEFAULT_URI_ID), productHandler::delete)
                .andRoute(POST(DEFAULT_URI_ID), productHandler::upload)
                .andRoute(POST(DEFAULT_URI), productHandler::saveAndUpload);

    }   // indicate response type is JSON with .and(contentType(MediaType.APPLICATION_JSON))
    // the request must have indicated content-type: json
}
