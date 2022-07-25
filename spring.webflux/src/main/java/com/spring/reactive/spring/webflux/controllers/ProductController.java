package com.spring.reactive.spring.webflux.controllers;

import com.spring.reactive.spring.webflux.Application;
import com.spring.reactive.spring.webflux.models.dao.ProductsDao;
import com.spring.reactive.spring.webflux.models.documents.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

@Controller
public class ProductController {

    @Autowired
    private ProductsDao productsDao;

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @GetMapping({"/listar", "/"})
    public String listar(Model model){
        Flux<Product> products = productsDao.findAll()
                .map(product -> product.setName(product.getName().toUpperCase()));

        products.subscribe(product -> log.info(product.getName()));

        model.addAttribute("products",products); // cuando se llame al método, se subscribirá al observable
        model.addAttribute("titulo","Listado de productos");

        return "listar";
    }
}
