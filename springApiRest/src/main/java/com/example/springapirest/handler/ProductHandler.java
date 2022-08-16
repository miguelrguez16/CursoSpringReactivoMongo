package com.example.springapirest.handler;

import com.example.springapirest.documents.Category;
import com.example.springapirest.documents.Product;
import com.example.springapirest.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.net.ResponseCache;
import java.net.URI;
import java.util.Date;

import static com.example.springapirest.utils.Utils.customName;

@Component
public class ProductHandler {
    @Autowired
    private ProductService productService;

    @Autowired
    private Validator validator;

    private static final Logger log = LoggerFactory.getLogger(ProductHandler.class);

    @Value("${config.uploads.path}")
    private String path;

    private final String URI_API_PRODUCTS = "/api/v2/products/";

    /**
     * @param serverRequest request fr
     * @return list of products
     */
    public Mono<ServerResponse> list(ServerRequest serverRequest) {

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.findAllProducts(), Product.class);
    }

    /**
     * @param serverRequest with the id of the product to see
     * @return  Mono<ServerResponse> with product
     */
    public Mono<ServerResponse> detail(ServerRequest serverRequest) {
        String idRequest = serverRequest.pathVariable("id");
        return productService.findProductById(idRequest)
                .doOnNext(product -> log.info("detail product ".concat(product.toString())))
                .flatMap(product -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(product)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Save a product from a post
     *
     * @param serverRequest with the product in Json format
     * @return the uri to find the product and the product
     */
    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        Mono<Product> productMono = serverRequest.bodyToMono(Product.class);
        return productMono.flatMap(product -> {
                    Errors errors = new BeanPropertyBindingResult(product,Product.class.getName());
                    validator.validate(product, errors);
                    if(errors.hasErrors()){
                        return Flux.fromIterable(errors.getFieldErrors())
                                .map(fieldErrors ->"El campo: " + fieldErrors.getField() + " " + fieldErrors.getDefaultMessage())
                                .collectList()
                                .flatMap(listErrors ->
                                        ServerResponse.badRequest().body(BodyInserters.fromValue(listErrors)));
                    }else{
                        if(product.getCreateAt()== null ) product.setCreateAt(new Date());
                        return productService.saveProduct(product)
                                .flatMap(productFromDB ->
                                ServerResponse.created(URI.create(URI_API_PRODUCTS.concat(productFromDB.getId())))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue(productFromDB)));
                    }

                });

    }

    public Mono<ServerResponse> edit(ServerRequest serverRequest) {
        String idRequest = serverRequest.pathVariable("id");
        Mono<Product> productMono = serverRequest.bodyToMono(Product.class);

        Mono<Product> productMonoDb = productService.findProductById(idRequest);

        return productMonoDb.zipWith(productMono, (db, req) -> {
                    db.setName(req.getName())
                            .setPrice(req.getPrice())
                            .setCategory(req.getCategory());
                    return db;
                }).flatMap(product ->
                        ServerResponse.created(URI.create("/api/v2/products/".concat(product.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(product)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        String idRequest = serverRequest.pathVariable("id");
        Mono<Product> productMonoDb = productService.findProductById(idRequest);

        return productMonoDb.flatMap(
                        product -> productService.deleteProduct(product) // returns no object
                                .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> upload(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");

        return serverRequest.multipartData()
                .map(stringPartMultiValueMap -> stringPartMultiValueMap.toSingleValueMap().get("file")) // name of the parameter
                .cast(FilePart.class) // cast to convert
                .flatMap(filePart -> productService.findProductById(id).
                        flatMap(product -> {
                            product.setPhoto(customName(filePart.filename()));
                            return filePart.transferTo(new File(path + product.getPhoto())).then(productService.saveProduct(product));
                        }))
                .flatMap(product -> ServerResponse.created(URI.create(URI_API_PRODUCTS.concat(product.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(product)))
                .switchIfEmpty(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> saveAndUpload(ServerRequest serverRequest){
        Mono<Product> productMono = serverRequest.multipartData()
                .map(stringPartMultiValueMap -> {
                    FormFieldPart name = (FormFieldPart) stringPartMultiValueMap.toSingleValueMap().get("name");
                    FormFieldPart price = (FormFieldPart) stringPartMultiValueMap.toSingleValueMap().get("price");
                    FormFieldPart categoryId = (FormFieldPart) stringPartMultiValueMap.toSingleValueMap().get("category.id");
                    FormFieldPart categoryName = (FormFieldPart) stringPartMultiValueMap.toSingleValueMap().get("category.name");
                    return new Product().setName(name.value()).setPrice(Double.valueOf(price.value())).setCategory(new Category().setName(categoryName.value()).setId(categoryId.value()));
                });



        return serverRequest.multipartData()
                .map(stringPartMultiValueMap -> stringPartMultiValueMap.toSingleValueMap().get("file")) // name of the parameter
                .cast(FilePart.class) // cast to convert
                .flatMap(filePart -> productMono //
                        .flatMap(p -> {
                            p.setPhoto(customName(filePart.filename()));
                            return filePart.transferTo(new File(path + p.getPhoto())).then(productService.saveProduct(p));
                        }))
                .flatMap(product -> ServerResponse.created(URI.create(URI_API_PRODUCTS.concat(product.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(product)))
                .switchIfEmpty(ServerResponse.notFound().build());

    }



}
