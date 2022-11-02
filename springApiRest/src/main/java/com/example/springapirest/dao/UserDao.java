package com.example.springapirest.dao;

import com.example.springapirest.documents.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserDao extends ReactiveMongoRepository<User, String> {

    Mono<User> findUserByUsername(String username);

}
