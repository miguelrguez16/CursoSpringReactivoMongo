package com.spring.reactive.spring.webflux.controllers;

import com.spring.reactive.spring.webflux.models.documents.Category;
import com.spring.reactive.spring.webflux.models.documents.Product;
import com.spring.reactive.spring.webflux.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.core.io.Resource;

import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@SessionAttributes("product") //guardar el producto en session, para que al editar se guarde el id
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Value("${config.uploads.path}")
    private String pathFile;

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);


    @ModelAttribute("categories")
    public Flux<Category> categories() {
        return productService.findAllCategories();
    }

    @GetMapping({"/listar", "/"})
    public Mono<String> listar(Model model) {
        Flux<Product> products = productService.findAllProductWithNameUpperCase()
                .doOnNext(product -> log.info(product.getName()));

        model.addAttribute("products", products); // cuando se llame al método, se subscribirá al observable
        model.addAttribute("titulo", "Listado de productos");

        return Mono.just("listar");
    }

    @GetMapping("/form")
    public Mono<String> create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("titulo", "Nuevo Producto");
        model.addAttribute("boton", "crear");

        return Mono.just("form");
    }

    @GetMapping("/form/{id}")
    public Mono<String> edit(@PathVariable String id, Model model) {
        Mono<Product> productMono = productService.findProductById(id)
                .doOnNext(product -> log.info("Edit" + product.getName()))
                .defaultIfEmpty(new Product()); // si se le pasa un id incorrecto

        model.addAttribute("titulo", "Editar Producto");
        model.addAttribute("product", productMono);
        model.addAttribute("boton", "editar");

        return Mono.just("form");
    }

    @GetMapping("/form-v2/{id}")
    public Mono<String> editV2(@PathVariable String id, Model model) {
        return productService.findProductById(id)
                .doOnNext(product -> {
                    log.info("Edit" + product.getName());
                    model.addAttribute("titulo", "Editar Producto");
                    model.addAttribute("product", product);
                })
                .flatMap(
                        product -> {
                            if (product.getId() == null) {
                                return Mono.error(new InterruptedException("No existe el producto"));
                            }
                            return Mono.just(product);
                        })
                .then(Mono.just("form"))
                .onErrorResume(throwable -> Mono.just("redirect:/listar?error=no+existe+el+producto"));


    }

    @PostMapping("/form")
    public Mono<String> save(@Valid @ModelAttribute("product") Product product,
                             BindingResult bindingResult, Model model,
                             @RequestPart(name = "file") FilePart file, SessionStatus sessionStatus) {

        if (bindingResult.hasErrors()) { // si falla se manda de nuevo, al tenerlo persistente se guarda el produto en session
            log.error(bindingResult.toString());
            model.addAttribute("titulo", "Error al validar el producto");
            model.addAttribute("boton", "editar");
            return Mono.just("form");
        } else {
            sessionStatus.setComplete();
            Mono<Category> categoryMono = productService.findCategoryById(product.getCategory().getId());
            return categoryMono.flatMap(category -> {
                        product.setCategory(category);
                        if (product.getCreateAt() == null) product.setCreateAt(new Date());
                        if (!file.filename().isEmpty()) product.setPhoto(UUID.randomUUID().toString() + "_"
                                +file.filename().replace(" ","")
                                .replace(":","")
                                .replace("//",""));
                        return productService.saveProduct(product);
                    })
                    .doOnNext(product1 -> log.info("Producto Guardado -> " + product1.toString()))
                    .flatMap(product1 -> {
                        if(!file.filename().isEmpty()) return file.transferTo(new File(pathFile + product.getPhoto()));
                        return Mono.empty();
                    })
                    .thenReturn("redirect:/listar?success=producto+guardado+con+exito");
        }

    }


    @GetMapping("/delete/{id}")
    public Mono<String> delete(@PathVariable String id) {
        return productService.findProductById(id)
                .defaultIfEmpty(new Product())
                .flatMap(product -> {
                    if (Objects.isNull(product)) {
                        return Mono.error(new InterruptedException("No existe el producto para eliminar"));
                    }
                    log.info("Eliminando producto: [" + product.getId() + "]");
                    return productService.deleteProduct(product);
                })
                .then(Mono.just("redirect:/listar?success=producto+eliminado"))
                .onErrorResume(throwable -> Mono.just("redirect:/listar?error=producto+no+existe"));

    }

    @GetMapping("/delete2/{id}")
    public Mono<String> delete2(@PathVariable String id) {
        return productService.findProductById(id)
                .defaultIfEmpty(new Product())
                .flatMap(product -> {
                    if (Objects.isNull(product)) {
                        return Mono.error(new InterruptedException("No existe el producto para eliminar"));
                    }
                    return Mono.just(product);
                })
                .flatMap(productService::deleteProduct)
                .then(Mono.just("redirect:/listar?success=producto+eliminado"))
                .onErrorResume(throwable -> Mono.just("redirect:/listar?error=producto+no+existe"));

    }


    @GetMapping("/listar-dataDriver")
    public String listarDataDriver(Model model) {
        Flux<Product> products = productService.findAllProductWithNameUpperCase()
                .delayElements(Duration.ofSeconds(1));

        products.subscribe(product -> log.info(product.getName()));
        // ReactiveDataDriver variable de contexto para manejar el tamaño del flujo
        model.addAttribute("products", new ReactiveDataDriverContextVariable(products, 2)); // cuando se llame al método, se subscribirá al observable
        model.addAttribute("titulo", "Listado de productos");

        return "listar";
    }

    @GetMapping("/listar-full")
    public String listarFull(Model model) {
        Flux<Product> products = productService.findAllProductWithNameUpperCase()
                .repeat(100);

        products.subscribe(product -> log.info(product.getName()));

        model.addAttribute("products", products); // cuando se llame al método, se subscribirá al observable
        model.addAttribute("titulo", "Listado de productos");

        return "listar";
    }

    @GetMapping("/listar-chuncked")
    public String listarChuncked(Model model) {
        Flux<Product> products = productService.findAllProductWithNameUpperCase()
                .repeat(100);

        products.subscribe(product -> log.info(product.getName()));

        model.addAttribute("products", products); // cuando se llame al método, se subscribirá al observable
        model.addAttribute("titulo", "Listado de productos");

        return "listar-chuncked";
    }


    @GetMapping("/view/{id}")
    public Mono<String> see(Model model, @PathVariable String id){
        return productService.findProductById(id)
                .doOnNext(
                        product -> {
                            model.addAttribute("product", product);
                            model.addAttribute("titulo", "Detalle del producto");
                        }).switchIfEmpty(Mono.just(new Product()))
                .flatMap(product -> {
                    if(product.getId()==null) {
                        return Mono.error(new InterruptedException("no existe prodcuto para visualizar"));
                    }
                    return Mono.just(product);
                }).then(Mono.just("view"))
                .onErrorResume(throwable -> Mono.just("redirect:/listar?error=producto+no+existe"));
    }

    //para guardar recursos en el cuerpo de la respuesta --> ResponseEntity
    @GetMapping("/uploads/img/{namePhoto:.+}")
    public Mono<ResponseEntity<Resource>> viewPhoto(@PathVariable String namePhoto) throws MalformedURLException {
        Path ruta = Paths.get(pathFile).resolve(namePhoto).toAbsolutePath();
        Resource image = new UrlResource(ruta.toUri());

        return Mono.just(
                ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFilename() + "\"")
                        .body(image)
        );
    }
}
