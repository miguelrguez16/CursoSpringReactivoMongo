package com.example.springapirest;

import com.example.springapirest.documents.Product;
import com.example.springapirest.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.util.Collections;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringApiRestApplicationTests {

    @Autowired
    private WebTestClient client;

    @Autowired
    private  ProductService productService;

    @Test
    void listProductsTest() {
        client.get().uri("api/v2/products").accept(MediaType.APPLICATION_JSON)
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
        Product product = productService.findByNameContainsIgnoreCase("Realme").block();
        assert product !=null;

        client.get().uri("api/v2/products/{id}", Collections.singletonMap("id", product.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Product.class)
                .consumeWith(response -> {
                    Product product1 = response.getResponseBody();
                    assert product1 != null;
                    Assertions.assertEquals("Realme",product1.getName());
                    Assertions.assertNotNull(product1.getId());
                    Assertions.assertEquals(9.99,product1.getPrice());
                });
//                .jsonPath("$.id").isNotEmpty()
//                .jsonPath("$.name").isEqualTo("Realme");
    }


}