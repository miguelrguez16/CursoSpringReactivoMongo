package com.example.springapirest.services.com;

import com.example.springapirest.documents.Role;
import com.example.springapirest.documents.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Flux<User> findAllUsers();

    Mono<User> findUserById(String id);

    Mono<User> findUserByUsername(String username);

    Mono<User> saveUser(User user);

    Mono<Void> deleteUser(User user);

    Flux<Role> findAllRoles();

    Mono<Role> findRoleById(String id);

    Mono<Role> findRoleByType(String type);

    Mono<Role> saveRole(Role role);

    Mono<Void> deleteRoler(Role role );
}
