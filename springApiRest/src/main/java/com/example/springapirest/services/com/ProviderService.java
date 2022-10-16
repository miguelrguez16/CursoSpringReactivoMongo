package com.example.springapirest.services.com;

import com.example.springapirest.documents.Provider;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProviderService {

    Mono<Provider> saveProvider(Provider provider);

    Mono<Void> deleteProvider(Provider provider);

    Mono<Provider> readByCompany(String company);

    Flux<Provider> findByCompanyAndPhone(String company, String phone);
}
