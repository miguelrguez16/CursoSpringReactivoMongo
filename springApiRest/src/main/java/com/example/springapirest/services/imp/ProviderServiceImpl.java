package com.example.springapirest.services.imp;

import com.example.springapirest.dao.ProviderDao;
import com.example.springapirest.documents.Provider;
import com.example.springapirest.services.com.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderDao providerDao;

    @Override
    public Mono<Provider> saveProvider(Provider provider) {
        return providerDao.save(provider);
    }

    @Override
    public Mono<Void> deleteProvider(Provider provider) {
        return providerDao.delete(provider);
    }

    @Override
    public Mono<Provider> readByCompany(String company) {
        return this.providerDao.findByCompany(company)
                .switchIfEmpty(Mono.error(new RuntimeException("Company not found ".concat(company))));
    }

    @Override
    public Flux<Provider> findByCompanyAndPhone(String company, String phone) {
        return providerDao.findByCompanyAndPhone(company,phone);
    }


}
