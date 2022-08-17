package com.example.springapirest;

import com.example.springapirest.documents.Category;
import com.example.springapirest.documents.Product;
import com.example.springapirest.services.ProductService;
import com.example.springapirest.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringApiRestApplicationTests {

    @Autowired
    private WebTestClient client;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;


    @Test
    void listProductsTest() {
        client.get().uri(Utils.DEFAULT_URI).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Product.class)
                .consumeWith(response -> {
                    List<Product> products = response.getResponseBody();
                    assert products != null;
                    products.forEach(System.out::println);
                });
    }

    @Test
    void detailTest() {
        Product product = productService.findProductByNameContainsIgnoreCase("Nvidia").block();
        assert product != null;

        client.get().uri(Utils.DEFAULT_URI_ID, Collections.singletonMap("id", product.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Product.class)
                .consumeWith(response -> {
                    Product product1 = response.getResponseBody();
                    assert product1 != null;
                    Assertions.assertEquals("Intel", product1.getName());
                    Assertions.assertNotNull(product1.getId());
                    Assertions.assertEquals(9.99, product1.getPrice());
                });
    }


    @Test
    void creatTestv2() {
        Category category = productService.findCategoryByNameContainsIgnoreCase("MARCA").block();
        Product product = new Product().setName("Raton_Logitech").setPrice(120.00).setCategory(category);

        client.post()
                .uri(Utils.DEFAULT_URI)
                .body(BodyInserters.fromValue(product))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Product.class)
                .consumeWith(response -> {
                    Product product1 = response.getResponseBody();
                    assert product1 != null;
                    Assertions.assertEquals("Raton_Logitech", product1.getName());
                    Assertions.assertEquals(120, product1.getPrice());
                    Assertions.assertNotNull(product1.getId());
                    System.out.println(product1);
                })
        ;
    }

    @Test
    void creatTest() {
        Category category = productService.findCategoryByNameContainsIgnoreCase("MARCA").block();
        Product product = new Product().setName("Raton_Logitech").setPrice(120.00).setCategory(category);

        client.post()
                .uri("api/products")
                .body(BodyInserters.fromValue(product))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(new ParameterizedTypeReference<LinkedHashMap<String, Object>>() {
                })
                .consumeWith(response -> {
                    Object o = Objects.requireNonNull(response.getResponseBody()).get("productSaved");
                    Product p = new ObjectMapper().convertValue(o, Product.class);
                    Assertions.assertEquals("Raton_Logitech", p.getName());
                    Assertions.assertEquals(120, p.getPrice());
                    Assertions.assertNotNull(p.getId());
                    System.out.println(p);
                })
        ;
    }


    @Test
    void editTest() {
        Product product = productService.findProductByNameContainsIgnoreCase("ASUS").block();
        Category category = productService.findCategoryByNameContainsIgnoreCase("CPU").block();
        assert product != null;
        assert category != null;

        product.setName("MX:MASTER").setCategory(category).setPrice(99.23);
        client.put()
                .uri(Utils.DEFAULT_URI_ID, Collections.singletonMap("id", product.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(product))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Product.class)
                .consumeWith(response -> {
                    Product product1 = response.getResponseBody();
                    assert product1 != null;
                    Assertions.assertEquals("MX:MASTER", product1.getName());
                    Assertions.assertEquals(99.23, product1.getPrice());
                    Assertions.assertNotNull(product1.getId());
                    System.out.println(product1);
                });
    }

    @Test
    void removeTest() {
        Product product = productService.findProductByNameContainsIgnoreCase("Nvidia").block();
        assert product != null;

        client.delete()
                .uri(Utils.DEFAULT_URI_ID, Collections.singletonMap("id", product.getId()))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty()
        ;

        client.get()
                .uri(Utils.DEFAULT_URI_ID, Collections.singletonMap("id", product.getId()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty()
        ;

    }
}