package com.spring.reactive.spring.webflux.services;

import com.spring.reactive.spring.webflux.models.dao.ProductsDao;
import com.spring.reactive.spring.webflux.models.documents.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductsDao productsDao;

    @Override
    public Flux<Product> findAll() {
        return productsDao.findAll();
    }

    @Override
    public Mono<Product> findById(String id) {
        return productsDao.findById(id);
    }
    @Override
    public Mono<Product> save(Product product) {
        return productsDao.save(product);
    }

    @Override
    public Mono<Void> delete(Product product) {
        return productsDao.delete(product);
    }
}
