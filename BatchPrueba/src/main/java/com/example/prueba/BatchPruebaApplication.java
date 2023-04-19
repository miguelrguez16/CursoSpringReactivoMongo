package com.example.prueba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class BatchPruebaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchPruebaApplication.class, args);
    }

}
