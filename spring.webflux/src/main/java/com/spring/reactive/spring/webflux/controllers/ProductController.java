package com.spring.reactive.spring.webflux.controllers;

import com.spring.reactive.spring.webflux.models.documents.Product;
import com.spring.reactive.spring.webflux.services.ProductService;
import jdk.jfr.Frequency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;

@SessionAttributes("product") //guardar el producto en session, para que al editar se guarde el id
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @GetMapping({"/listar", "/"})
    public Mono<String> listar(Model model){
        Flux<Product> products = productService.findAllWithNameUpperCase()
                .doOnNext(product -> log.info(product.getName()));

        model.addAttribute("products",products); // cuando se llame al método, se subscribirá al observable
        model.addAttribute("titulo","Listado de productos");

        return Mono.just("listar");
    }
    @GetMapping("/form")
    public Mono<String> create(Model model){
        model.addAttribute("product", new Product());
        model.addAttribute("titulo","Nuevo Producto");
        model.addAttribute("boton","crear");

        return Mono.just("form");
    }
    @GetMapping("/form/{id}")
    public Mono<String> edit(@PathVariable String id, Model model){
        Mono<Product> productMono = productService.findById(id)
                .doOnNext(product ->log.info("Edit" + product.getName()))
                .defaultIfEmpty(new Product()); // si se le pasa un id incorrecto

        model.addAttribute("titulo","Editar Producto");
        model.addAttribute("product",productMono);
        model.addAttribute("boton","editar");

        return Mono.just("form");
    }

    @GetMapping("/form-v2/{id}")
    public Mono<String> editV2(@PathVariable String id, Model model){
        return  productService.findById(id)
                .doOnNext(product -> {
                    log.info("Edit" + product.getName());
                    model.addAttribute("titulo","Editar Producto");
                    model.addAttribute("product",product);
                })
                .flatMap(
                        product -> {
                            if(product.getId()==null) {
                                return Mono.error(new InterruptedException("No existe el producto"));
                            }
                            return Mono.just(product);
                        })
                .then(Mono.just("form"))
                .onErrorResume(throwable -> Mono.just("redirect:/listar?error=no+existe+el+producto"));



    }

    @PostMapping("/form")
    public Mono<String> save(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult, Model model, SessionStatus sessionStatus){
        if(bindingResult.hasErrors()){ // si falla se manda de nuevo, al tenerlo persistente se guarda el prodcuto en session
            model.addAttribute("titulo","Error al validar el producto");
            model.addAttribute("boton","editar");
            return Mono.just("form");
        }else{

            if(product.getCreateAt()==null)
                product.setCreateAt(new Date());

            return productService.save(product).
                    doOnNext(product1 -> log.info("Producto Guardado" + product1.getName()))
                    .thenReturn("redirect:/listar?success=producto+guardado+con+exito");
        }

    }


    @GetMapping("/delete/{id}")
    public Mono<String> delete(@PathVariable String id){
        return productService.findById(id)
                .defaultIfEmpty(new Product())
                .flatMap(product -> {
                    if(Objects.isNull(product)){
                        return Mono.error(new InterruptedException("No existe el producto para eliminar"));
                    }
                    log.info("Eliminando producto: [" + product.getId()+"]");
                    return productService.delete(product);
                })
                .then(Mono.just("redirect:/listar?success=producto+eliminado"))
                .onErrorResume(throwable -> Mono.just("redirect:/listar?error=producto+no+existe"));

    }

    @GetMapping("/delete2/{id}")
    public Mono<String> delete2(@PathVariable String id){
        return productService.findById(id)
                .defaultIfEmpty(new Product())
                .flatMap(product -> {
                    if(Objects.isNull(product)){
                        return Mono.error(new InterruptedException("No existe el producto para eliminar"));
                    }
                    return Mono.just(product);
                })
                .flatMap(productService::delete)
                .then(Mono.just("redirect:/listar?success=producto+eliminado"))
                .onErrorResume(throwable -> Mono.just("redirect:/listar?error=producto+no+existe"));

    }


    @GetMapping("/listar-dataDriver")
    public String listarDataDriver(Model model){
        Flux<Product> products = productService.findAllWithNameUpperCase()
                .delayElements(Duration.ofSeconds(1));

        products.subscribe(product -> log.info(product.getName()));
        // ReactiveDataDriver variable de contexto para manejar el tamaño del flujo
        model.addAttribute("products", new ReactiveDataDriverContextVariable(products, 2)); // cuando se llame al método, se subscribirá al observable
        model.addAttribute("titulo","Listado de productos");

        return "listar";
    }

    @GetMapping("/listar-full")
    public String listarFull(Model model){
        Flux<Product> products = productService.findAllWithNameUpperCase()
                .repeat(100);

        products.subscribe(product -> log.info(product.getName()));

        model.addAttribute("products",products); // cuando se llame al método, se subscribirá al observable
        model.addAttribute("titulo","Listado de productos");

        return "listar";
    }

    @GetMapping("/listar-chuncked")
    public String listarChuncked(Model model){
        Flux<Product> products = productService.findAllWithNameUpperCase()
                .repeat(100);

        products.subscribe(product -> log.info(product.getName()));

        model.addAttribute("products",products); // cuando se llame al método, se subscribirá al observable
        model.addAttribute("titulo","Listado de productos");

        return "listar-chuncked";
    }


}
