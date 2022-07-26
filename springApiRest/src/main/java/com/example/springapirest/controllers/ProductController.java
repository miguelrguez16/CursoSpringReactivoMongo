package com.example.springapirest.controllers;

import com.example.springapirest.documents.Product;
import com.example.springapirest.services.com.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.File;
import java.net.URI;
import java.util.*;

import static com.example.springapirest.utils.Utils.customName;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Value("${config.uploads.path}")
    private String path;

    @Autowired
    private ProductService productService;

    @GetMapping("/listar")
    public Flux<Product> listProduct() {
        return productService.findAllProducts();
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<Product>>> listProductEntity() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(productService.findAllProducts())
        );
    }

    @GetMapping("/listar/{id}")
    public Mono<ResponseEntity<Mono<Product>>> viewDetail(@PathVariable String id) {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(productService.findProductById(id))
        );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> viewDetail2(@PathVariable String id) {
        return productService.findProductById(id)
                .map(product ->
                        ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                                .body(product)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/names")
    public Mono<ResponseEntity<Flux<Map<String,String>>>> nameFilms(){
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(productService.listAllFilmNames())
        );
    }

    @GetMapping("/categoriesCount")
    public Mono<ResponseEntity<Flux<Map<String,Object>>>> totalCategoriesByProducts(){
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(productService.getTotalProductsByCategory())
        );
    }

    /**
     * Save product
     *
     * @param productMono to save in DB
     * @return the product save en DB
     */
    @PostMapping()
    public Mono<ResponseEntity<Map<String, Object>>> save(@Valid @RequestBody Mono<Product> productMono) {
        Map<String, Object> response = new HashMap<>();
        return productMono.flatMap(product -> {
            if (product.getCreateAt() == null) product.setCreateAt(new Date());

            return productService.saveProduct(product)
                    .doOnNext(product1 -> log.info("SAVE: ".concat(product1.toString())))
                    .map(saveProduct -> {
                        response.put("productSaved", saveProduct);
                        response.put("status", "success");
                        response.put("timestamp", new Date());
                        return ResponseEntity.created(URI.create("/api/products/".concat(saveProduct.getId())))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(response);
                    });
        }).onErrorResume(throwable ->
                Mono.just(throwable).cast(WebExchangeBindException.class)
                        .flatMap(e -> Mono.just(e.getFieldErrors()))// obtenemos la lista de errores
                        .flatMapMany(Flux::fromIterable) // obtenemos el elmento con errores
                        .map(fieldError -> "el campo: " + fieldError.getField() + ". " + fieldError.getDefaultMessage()) //lo convertimos a String
                        .collectList() //lo convertimos a Mono, mediante una lista
                        .flatMap(stringsList -> {
                            response.put("errors", stringsList);
                            response.put("status", HttpStatus.BAD_REQUEST.value());
                            response.put("timestamp", new Date());
                            return Mono.just(ResponseEntity.badRequest().body(response));
                        })
        );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Product>> actualizar(@PathVariable String id, @RequestBody Product productRequest) {
        return productService.findProductById(id).flatMap(productToSave -> {
                    productToSave.setName(productRequest.getName());
                    productToSave.setCategory(productRequest.getCategory());
                    productToSave.setPrice(productRequest.getPrice());
                    return productService.saveProduct(productToSave);
                }).doOnNext(productInfo -> log.info("UPDATE: ".concat(productInfo.toString())))
                .map(saveProduct ->
                        ResponseEntity.created(URI.create("/api/products/" + saveProduct.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(saveProduct))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorMap(RuntimeException::new);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return productService.findProductById(id)
                .flatMap(product -> productService.deleteProduct(product)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                ).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND))
                .onErrorMap(RuntimeException::new);
    }

    @PostMapping("upload/{id}")
    public Mono<ResponseEntity<Product>> uploadImage(@PathVariable String id, @RequestPart FilePart file) {
        return productService.findProductById(id)
                .flatMap(product -> {
                    product.setPhoto(customName(file.filename()));
                    return file.transferTo(new File(path.concat(product.getPhoto()))).then(productService.saveProduct(product));
                }).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
