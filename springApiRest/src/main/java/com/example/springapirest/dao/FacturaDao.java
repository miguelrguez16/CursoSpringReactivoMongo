package com.example.springapirest.dao;

import com.example.springapirest.documents.Factura;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FacturaDao extends ReactiveMongoRepository<Factura, String> {
}
