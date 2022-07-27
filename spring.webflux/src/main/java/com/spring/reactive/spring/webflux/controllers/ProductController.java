package com.spring.reactive.spring.webflux.controllers;

import com.spring.reactive.spring.webflux.models.documents.Product;
import com.spring.reactive.spring.webflux.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

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

        return Mono.just("form");
    }
    @GetMapping("/form/{id}")
    public Mono<String> edit(@PathVariable String id, Model model){
        Mono<Product> productMono = productService.findById(id)
                .doOnNext(product ->log.info("Edit" + product.getName()))
                .defaultIfEmpty(new Product()); // si se le pasa un id incorrecto

        model.addAttribute("titulo","Editar Producto");
        model.addAttribute("product",productMono);
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
    public Mono<String> save(Product product, SessionStatus sessionStatus){
        sessionStatus.setComplete();
        return productService.save(product).
                doOnNext(product1 -> log.info("Producto Guardado" + product1.getName()))
                .thenReturn("redirect:/listar");
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
