package com.example.springapirest.services.imp;

import com.example.springapirest.dao.FacturaDao;
import com.example.springapirest.documents.Factura;
import com.example.springapirest.services.com.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaDao facturaDao;

    @Override
    public Flux<Factura> findAllFactura() {
        return facturaDao.findAll();
    }

    @Override
    public Mono<Factura> findFacturaById(String id) {
        return facturaDao.findById(id);
    }

    @Override
    public Mono<Factura> saveFactura(Factura factura) {
        return facturaDao.save(factura);
    }

    @Override
    public Mono<Void> deleteFactura(Factura factura) {
        return facturaDao.delete(factura);
    }
}
