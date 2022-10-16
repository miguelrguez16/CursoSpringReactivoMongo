package com.example.springapirest.dao;

import com.example.springapirest.documents.Provider;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProviderDao extends ReactiveMongoRepository<Provider, String> {

    Mono<Provider> findByCompany(String company);

    Flux<Provider> findByCompanyAndPhone(String company, String phone);


}
