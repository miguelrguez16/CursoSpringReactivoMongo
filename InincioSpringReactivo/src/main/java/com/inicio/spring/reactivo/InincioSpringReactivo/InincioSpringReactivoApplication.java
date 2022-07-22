package com.inicio.spring.reactivo.InincioSpringReactivo;

import com.inicio.spring.reactivo.InincioSpringReactivo.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class InincioSpringReactivoApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(InincioSpringReactivoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ejemploFlatMap();
    }

    public void ejemploFlatMap() throws Exception {
        List<String> usersList = new ArrayList<>();
        usersList.add("Andres Guzman");
        usersList.add("Pedro Fernandez");
        usersList.add("Miguel Rguez");
        usersList.add("Diego Farcia");
        usersList.add("Bruce Lee");
        usersList.add("Bruce Willies");

        Flux.fromIterable(usersList)
                .map(name -> {
                    String[] namesUser = name.split(" ");
                    return new User().setName(namesUser[0]).setLastName(namesUser[1]);
                })
//              .filter(user -> user.getName().equalsIgnoreCase("bruce"))
                // con flat Map genera
                .flatMap(user -> { // convierte en un nuevo flujo
                    if(user.getName().equalsIgnoreCase("bruce"))
                        return Mono.just(user); // emite un usuario
                    else
                        return Mono.empty();
                })
                .map( user -> {
                    String nameUser = user.getName().toLowerCase();
                    user.setName(nameUser);
                    return user;
                })
                .subscribe(u-> log.info(u.toString()));

    }
    public void ejemploIterable() throws Exception {
        List<String> usersList = new ArrayList<>();
        usersList.add("Andres Guzman");
        usersList.add("Pedro Fernandez");
        usersList.add("Miguel Rguez");
        usersList.add("Diego Farcia");
        usersList.add("Bruce Lee");
        usersList.add("Bruce Willies");


        Flux<String> nombres = Flux.fromIterable(usersList);


/*
        Flux<String> fluxNames = Flux.just("Andres Guzman", "Pedro Fernandez", "Miguel Rguez", "Diego Garcia", "Bruce Lee", "Bruce Willies");
        //.map(String::toUpperCase)
*/
        Flux<User> users = nombres.map(name -> {
                    String[] namesArray = name.split(" ");
                    return new User().setName(namesArray[0].toUpperCase()).setLastName(namesArray[1].toUpperCase());
                })
                .filter(user -> user.getName().equalsIgnoreCase("bruce"))
                .doOnNext(user -> {
                    if (user.getName().isEmpty()) {
                        throw new RuntimeException("empty value on flux");
                    } else {
                        System.out.println(user.getName().concat(" " + user.getLastName()));
                    }
                })
                .map(user -> user.setName(user.getName().toLowerCase())
                );

        //fluxNames.subscribe(e -> log.info(e));
        //fluxNames.subscribe(log::error);
        users.subscribe(user -> log.info(user.toString()),
                error -> log.error(error.getMessage()),
                () -> log.info("Fin del flujo"));


    }
}
