package com.example.springapirest;

import com.example.springapirest.documents.*;
import com.example.springapirest.services.com.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SpringApiRestApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringApiRestApplication.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private FacturaService facturaService;

    @Autowired
    private UserService userService;

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
        reactiveMongoTemplate.dropCollection("provider").subscribe();
        reactiveMongoTemplate.dropCollection("customer").subscribe();
        reactiveMongoTemplate.dropCollection("factura").subscribe();
        reactiveMongoTemplate.dropCollection("user").subscribe();
        reactiveMongoTemplate.dropCollection("role").subscribe();

        Category foodCategory = new Category().setName("FOOD");
        Category drinksCategory = new Category().setName("DRINKS");
        Category electronicCategory = new Category().setName("ELECTRONIC");
        Category clothesCategory = new Category().setName("CLOTHES");

        Provider unioviProvider = new Provider().setCompany("uniovi");
        Provider provider2 = new Provider().setCompany("ADIDAS");
        Provider provider3 = new Provider().setCompany("DRINKS_BULL");
        Provider provider4 = new Provider().setCompany("Hacendado");
        Flux.just(unioviProvider, provider2, provider3, provider4)
                .flatMap(providerService::saveProvider)
                .subscribe();

        Product tmp1 = new Product().setName("HP_VICTUS").setPrice(999.99).setCategory(electronicCategory).setProductProvider(unioviProvider);
        Product tmp2 = new Product().setName("TOSHIVA").setPrice(99.0).setCategory(electronicCategory).setProductProvider(unioviProvider);
        Product tmp3 = new Product().setName("ASUS").setPrice(33.99).setCategory(electronicCategory).setProductProvider(unioviProvider);
        Product tmp4 = new Product().setName("Ryzen_5").setPrice(9.99).setCategory(electronicCategory).setProductProvider(unioviProvider);
        Product tmp5 = new Product().setName("Intel").setPrice(45.99).setCategory(electronicCategory).setProductProvider(unioviProvider);
        Product tmp6 = new Product().setName("Snapdragon").setPrice(36.00).setCategory(electronicCategory).setProductProvider(unioviProvider);
        Product tmp7 = new Product().setName("naranja").setPrice(119.99).setCategory(drinksCategory).setProductProvider(unioviProvider);
        Product tmp8 = new Product().setName("zumo").setPrice(684784.99).setCategory(drinksCategory).setProductProvider(provider4);
        Product tmp9 = new Product().setName("Hub_Green").setPrice(23.1).setCategory(drinksCategory).setProductProvider(provider3);
        Product tmp10 = new Product().setName("Watch").setPrice(1000.0).setCategory(drinksCategory).setProductProvider(provider2);
        Product tmp11 = new Product().setName("chocolate").setPrice(10.0).setCategory(foodCategory).setProductProvider(provider2);
        Product tmp12 = new Product().setName("muffin").setPrice(1000.0).setCategory(foodCategory).setProductProvider(provider2);
        Product tmp13 = new Product().setName("Watch").setPrice(1000.0).setCategory(clothesCategory).setProductProvider(provider2);
        Product tmp14 = new Product().setName("wallet").setPrice(1000.0).setCategory(clothesCategory).setProductProvider(provider2);
        Product tmp15 = new Product().setName("shirt").setPrice(1000.0).setCategory(clothesCategory).setProductProvider(provider2);
        Product tmp16 = new Product().setName("t-shirt").setPrice(1000.0).setCategory(clothesCategory).setProductProvider(provider2);
        Product tmp17 = new Product().setName("sweet").setPrice(1000.0).setCategory(clothesCategory).setProductProvider(provider2);
        Product tmp18 = new Product().setName("dress").setPrice(1000.0).setCategory(clothesCategory).setProductProvider(provider2);

        Flux.just(foodCategory, drinksCategory, electronicCategory, clothesCategory)
                .flatMap(productService::saveCategory)
                //.doOnNext(category -> log.info(category.toString()))
                .thenMany(
                            (
                                Flux.just(tmp1, tmp2, tmp3, tmp4, tmp5, tmp6, tmp7, tmp8, tmp9, tmp10, tmp11,tmp12,tmp13,tmp14,tmp15,tmp16,tmp17,tmp18)
                            ).flatMap(product -> {
                                product.setCreateAt(new Date());
                                return productService.saveProduct(product);
                            }).doOnNext(product -> log.info(product.toString()))
                ).blockLast();
        List<Product> listaProductosFacturea = new ArrayList<>();
        productService.findAllProducts().map(listaProductosFacturea::add).blockLast();

        Factura factura1 = new Factura().setProductList(listaProductosFacturea).setInvoiceDate(new Date());
        listaProductosFacturea.removeIf(product -> product.getName()=="dress");
        Factura factura2 = new Factura().setProductList(listaProductosFacturea).setInvoiceDate(new Date());

        Flux.just(factura1,factura2)
                .flatMap(facturaService::saveFactura)
                .blockFirst();
        List<Factura> facturaList = new ArrayList<>();
        facturaService.findAllFactura().map(facturaList::add).blockFirst();
        Customer customer1 = new Customer().setName("John").setLastnames("Blank").setFacturaList(facturaList);
        System.out.println(customer1.toString());
        Flux.just(customer1).flatMap(customerService::saveCustomer).blockFirst();

        Role r = new Role().setType("ADMIN");
        r = userService.saveRole(r).block();

        User u = new User().setUsername("ADMIN").setPassword("ADMIN").addRole(r);
        userService.saveUser(u).subscribe();
        log.info("updated collection");

    }
}
