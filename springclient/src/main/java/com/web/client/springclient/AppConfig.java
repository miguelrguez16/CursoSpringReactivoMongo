package com.web.client.springclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${config.base.endpoint.client}")
    private String uriClient;


    @Bean
    public WebClient registrarWebClient() {
        return WebClient.url(uriClient);
    }}
