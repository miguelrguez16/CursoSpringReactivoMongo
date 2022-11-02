package com.example.springapirest.dao;

import com.example.springapirest.documents.Role;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface RoleDao extends ReactiveMongoRepository<Role, String> {

    Mono<Role> findRoleByType(String type);

}
