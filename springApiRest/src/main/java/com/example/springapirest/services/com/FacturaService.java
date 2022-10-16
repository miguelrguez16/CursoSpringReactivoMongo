package com.example.springapirest.services.com;

import com.example.springapirest.documents.Factura;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FacturaService {

    Flux<Factura> findAllFactura();

    Mono<Factura> findFacturaById(String id);

    Mono<Factura> saveFactura(Factura factura);

    Mono<Void> deleteFactura(Factura factura);

}
