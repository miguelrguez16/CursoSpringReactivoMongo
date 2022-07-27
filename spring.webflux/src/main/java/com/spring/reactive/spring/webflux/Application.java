package com.spring.reactive.spring.webflux;

import com.spring.reactive.spring.webflux.models.dao.ProductsDao;
import com.spring.reactive.spring.webflux.models.documents.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private ProductsDao productsDao;

	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("removing collection");
		reactiveMongoTemplate.dropCollection("products")
						.subscribe();

		Flux.just(
			new Product().setName("HP VICTUS").setPrice(999.99),
			new Product().setName("TOSHIVA").setPrice(99.0),
			new Product().setName("ASUS").setPrice(33.99),
			new Product().setName("Realme").setPrice(9.99),
			new Product().setName("Ryzen 5").setPrice(9.99),
			new Product().setName("Tronsmart Element").setPrice(45.99),
			new Product().setName("Ebook").setPrice(36.00),
			new Product().setName("Huawei FreeBuds Pro").setPrice(119.99),
			new Product().setName("Nvidia").setPrice(684784.99)
		).flatMap( product ->{
			product.setCreateAt(new Date());
			return productsDao.save(product);
		})
				//obtiene el observable (flux o MOno) y lo aplana para crear un nuevo flujo de productos
		.subscribe(product -> log.info(product.getName() + " -> [" + product.getId() + "]"));

	}
}
