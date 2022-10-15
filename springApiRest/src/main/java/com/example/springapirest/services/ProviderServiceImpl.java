package com.example.springapirest.services;

import com.example.springapirest.dao.ProviderDao;
import com.example.springapirest.documents.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderDao providerDao;

    /**
     * @param company
     * @return
     */
    @Override
    public Mono<Provider> readByCompany(String company) {
        return this.providerDao.findByCompany(company)
                .switchIfEmpty(Mono.error(new RuntimeException("Company not found ".concat(company))));
    }

    /**
     * @param company
     * @param phone
     * @param note
     * @return
     */
    @Override
    public Flux<Provider> findByCompanyAndPhoneAndNoteNullSafe(String company, String phone, String note) {
        return null;
    }
}
