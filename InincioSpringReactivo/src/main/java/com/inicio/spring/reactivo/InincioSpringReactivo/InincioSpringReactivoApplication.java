package com.inicio.spring.reactivo.InincioSpringReactivo;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.Locale;

@SpringBootApplication
public class InincioSpringReactivoApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(InincioSpringReactivoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Flux<String> flujoNombre = Flux.just("Andres", "Pedro", "Miguel", "Strings.EMPTY", "Diego")
                .map(String::toUpperCase)
                .doOnNext(e -> {
                    if (e.isEmpty()) {
                        throw new RuntimeException("NO PUEDES PASAR");
                    } else {
                        System.out.println(e);
                    }
                })
                .map(String::toLowerCase);

        //flujoNombre.subscribe(e -> log.info(e));
        //flujoNombre.subscribe(log::error);
        flujoNombre.subscribe(log::info,
                error -> log.error(error.getMessage()),
                new Runnable() {
                    @Override
                    public void run() {
                        log.info("FINIQUITAO");
                    }
                });
    }
}
