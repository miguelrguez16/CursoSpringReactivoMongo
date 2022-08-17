package com.web.client.springclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${config.base.endpoint.client}")
    private String uriClient;


    @Bean
    public WebClient registerWebClient(){
        return WebClient.create(uriClient);
    }
}
