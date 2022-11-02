package com.example.springapirest.controllers;

import com.example.springapirest.documents.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping()
public class WelcomeController {
    @GetMapping
    public Mono<ResponseEntity<String>> listProductEntity() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("HellO")
        );
    }

    @PostMapping("/login")
    public Mono<User> login(ServerWebExchange exchange) {

        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(User.class)
                .doOnNext(userDetails -> {
                    addTokenHeader(exchange.getResponse(), userDetails); // your job to code it the way you want
                });
    }

    private void addTokenHeader(ServerHttpResponse response, User userDetails) {
    }
}
