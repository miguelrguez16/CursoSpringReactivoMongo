package com.example.springapirest;

import com.example.springapirest.documents.Category;
import com.example.springapirest.documents.Product;
import com.example.springapirest.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;
@EnableEurekaClient
@SpringBootApplication
public class SpringApiRestApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringApiRestApplication.class);

	@Autowired
	private ProductService productService;

	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringApiRestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("removing collection");
		reactiveMongoTemplate.dropCollection("products").subscribe();
		reactiveMongoTemplate.dropCollection("categories").subscribe();

		Category marca = new Category().setName("MARCA");
		Category cpu = new Category().setName("CPU");
		Category electronic = new Category().setName("ELECTRONIC");

		Flux.just(marca, cpu, electronic)
				.flatMap(productService::saveCategory)
				.doOnNext(category -> log.info(category.toString()))
				.thenMany(
						Flux.just(
								new Product().setName("HP VICTUS").setPrice(999.99).setCategory(marca),
								new Product().setName("TOSHIVA").setPrice(99.0).setCategory(marca),
								new Product().setName("ASUS").setPrice(33.99).setCategory(marca),
								new Product().setName("Realme").setPrice(9.99).setCategory(marca),
								new Product().setName("Ryzen 5").setPrice(9.99).setCategory(cpu),
								new Product().setName("Intel").setPrice(45.99).setCategory(cpu),
								new Product().setName("Snapdragon").setPrice(36.00).setCategory(cpu),
								new Product().setName("Apple M1").setPrice(119.99).setCategory(cpu),
								new Product().setName("Nvidia").setPrice(684784.99).setCategory(marca),
								new Product().setName("Hub Green").setPrice(23.1).setCategory(electronic),
								new Product().setName("Watch").setPrice(1000.0).setCategory(electronic)
						).flatMap(product -> {
							product.setCreateAt(new Date());
							return productService.saveProduct(product);
						}).doOnNext(product -> log.info(product.toString()))
				)
				.subscribe();

		log.info("updated collection");

	}
}
