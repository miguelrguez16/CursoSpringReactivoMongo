package com.example.springapirest.services;


import com.example.springapirest.documents.Provider;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProviderService {

    Mono<Provider> readByCompany(String company);

    Flux<Provider> findByCompanyAndPhoneAndNoteNullSafe(String company, String phone, String note);
}
