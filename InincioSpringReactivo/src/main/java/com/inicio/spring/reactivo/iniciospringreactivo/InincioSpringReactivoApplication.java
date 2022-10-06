package com.inicio.spring.reactivo.iniciospringreactivo;

import com.inicio.spring.reactivo.iniciospringreactivo.models.Comments;
import com.inicio.spring.reactivo.iniciospringreactivo.models.User;
import com.inicio.spring.reactivo.iniciospringreactivo.models.UserWithComments;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class InincioSpringReactivoApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(InincioSpringReactivoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ejemploContraPresionLimitRate();
    }


    public void ejemploContraPresionLimitRate() throws Exception {
        Flux.range(1, 10)
                .log()
                .limitRate(5)
                .subscribe();
    }
    public void ejemploContraPresion() throws Exception {
        Flux.range(1,10)
                .log() // request(unbounded)
                .subscribe(new Subscriber<Integer>() {
                    private Subscription subscription;
                    private Integer limit = 5; // procesamos por lotes del valor de limit
                    private Integer consumed = 0;
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        subscription.request(limit); // cantidad maxima posible
                        consumed++;

                    }

                    @Override
                    public void onNext(Integer integer) {
                        log.info(integer.toString());
                        consumed++;
                        if(consumed== limit){
                            consumed = 0;
                            subscription.request(limit);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void ejemploCreateFlux() throws Exception{
        Flux.create(emitter ->{
            Timer timer = new Timer();
            timer.schedule(new TimerTask(){
                private int cont = 0;
                @Override
                public void run() {
                    emitter.next(++cont); // proximo elemento a emitir
                    if(cont == 10){
                        timer.cancel();
                        emitter.complete(); //marcarlo como completado
                    }
                    if (cont == 5){
                        timer.cancel();
                        emitter.error( new InterruptedException("Flux en 5"));
                    }
                }
            },1000,1000);
        })
//            .doOnNext( o -> log.info(o.toString()))
//            .doOnComplete( () -> log.info("END"))
            .subscribe(
                    next -> log.info(next.toString()),
                    error -> log.error(error.getMessage()),
                    ()-> log.info("END")
            ); // al subscribirnos pedimos al flujo que nos envie to do, la contrapresion evita esto
            // contrapresion el suscriptor pide menos carga
    }

    public void ejemploIntervaloInfinito() throws Exception{
        CountDownLatch latch = new CountDownLatch(1);

        Flux.interval(Duration.ofSeconds(1))
//                .doOnTerminate( ()-> latch.countDown())
                .doOnTerminate(latch::countDown)   // se ejecuta falle o no falle el flujo
                .flatMap( i -> {
                    if(i >= 5){
                        return Flux.error(new InterruptedException("Solo hasta 5"));
                    }
                    else {
                        return Flux.just(i);
                    }
                })
                .map(a -> "Hola " + a)
//                .doOnNext(i -> log.info(i.toString()))
//                .blockLast();
                .retry(2) // número de veces que intentará ejecutarse despues de un error
                .subscribe(i -> log.info(i.toString()) , e -> log.error(e.getMessage()));

        latch.await();
    }

    public void exampleRango() throws Exception{
        Flux<Integer> rangos = Flux.range(1,12);
        rangos.delayElements(Duration.ofSeconds(1))
            .doOnNext(i -> log.info(i.toString()))
                .blockLast();

        //Thread.sleep(13000); solo con un subscribe
    }

    public void exampleInterval() throws Exception{
        Flux<Integer> rangos = Flux.range(1,12);
        Flux<Long> relay = Flux.interval(Duration.ofSeconds(1));


        rangos.zipWith(relay, (rango, retraso)-> rango)
                .doOnNext( i -> log.info(i.toString()))
                .blockLast(); //subscribir bloqueando para que no se ejecute en segundo plano
    }

    // flux de un range
    public void exampleRangeZipWith() throws Exception{
        Flux<Integer> rangos = Flux.range(0,4);
        Flux.just(1,2,3,4)
                .map(integer -> integer*2)
                .zipWith( rangos, (flujoUno, flujoDos) -> String.format("Primer flux %d y Segundo Flux %d", flujoUno, flujoDos))
                .subscribe(txt -> log.info(txt));
    }

    public void ejemploUsuarioComentariosZipWithforma2() throws Exception {
        /*
        Mono<User> usuarioMono = Mono.fromCallable(() -> new User().setName("Miguel").setLastName("Rguez"));

        Mono<Comments> comentariosUsuarioMono = Mono.fromCallable(() -> {
            Comments comentarios = new Comments();
            comentarios.addComment("Hola pepe, qué tal!");
            comentarios.addComment("Mañana voy a la playa!");
            comentarios.addComment("Estoy tomando el curso de spring con reactor");
            return comentarios;
        });

        Mono<UserWithComments> usuarioConComentarios = usuarioMono.zipWith(comentariosUsuarioMono)
            .map(tuple -> {
                User u = tuple.getT1();
                Comments c = tuple.getT2();
            return new UserWithComments(u, c);
        });

        usuarioConComentarios.subscribe(uc -> log.info(uc.toString()));
*/

        Mono<User> userMono = Mono.fromCallable(() -> new User().setName("Miguel").setLastName("Rodriguesz"));
        Mono<Comments> commentsMono = Mono.fromCallable(() -> {
            Comments comments = new Comments();
            comments.addComment("Comentario 1");
            comments.addComment("Comentario 2");
            comments.addComment("Comentario 3");
            return comments;
        });

        userMono.zipWith(commentsMono)
                .map(tuple -> {
                    User u = tuple.getT1();
                    Comments c = tuple.getT2();
                    return new UserWithComments(u,c);
                }).subscribe(usuarioConComentarios -> log.info(usuarioConComentarios.toString()));

    }

    public void ejemploUsuarioComentariosZipWith() throws Exception {
        Mono<User> userMono = Mono.fromCallable(() -> new User().setName("Miguel").setLastName("Rodriguesz"));
        Mono<Comments> commentsMono = Mono.fromCallable(() -> {
            Comments comments = new Comments();
            comments.addComment("Comentario 1");
            comments.addComment("Comentario 2");
            comments.addComment("Comentario 3");
            return comments;
        });

        // combinar dos Mono
        userMono.zipWith(commentsMono, (user, comments) -> new UserWithComments(user, comments))
                .subscribe(userWithComments -> log.info(userWithComments.toString()));

    }

    /*
     *   Convierte dos flujos en uno único, utilizando flatMap
     */
    public void ejemploUsuarioComentariosFlatMap() throws Exception {
        Mono<User> userMono = Mono.fromCallable(() -> new User().setName("Miguel").setLastName("Rodriguesz"));
        Mono<Comments> commentsMono = Mono.fromCallable(() -> {
            Comments comments = new Comments();
            comments.addComment("Comentario 1");
            comments.addComment("Comentario 2");
            comments.addComment("Comentario 3");
            return comments;
        });

        // combinar dos Mono
        userMono
                .flatMap(user -> commentsMono
                        .map(comments -> new UserWithComments(user, comments)))
                .subscribe(userWithComments -> log.info(userWithComments.toString()));

    }

    public void ejemploFluxAMono() throws Exception {
        List<User> userListFlux = this.rellenarUsuarios();


        Flux.fromIterable(userListFlux)
                .map(user ->
                        user.getName().toUpperCase().concat(" " + user.getLastName().toUpperCase())
                )
                .collectList() // convierte el conjunto a una lista
                //.subscribe(lista-> log.info(lista.toString()));    // imprimimos la lista
                .subscribe(lista -> lista.forEach(item -> log.info(item.toString())));
    }

    public void ejemploUserToString() throws Exception {
        final List<User> usersList = this.rellenarUsuarios();

        Flux.fromIterable(usersList)
                .map(user ->
                        user.getName().toUpperCase().concat(user.getLastName().toUpperCase())
                )
                .flatMap(name -> {
                    if (name.contains("BRUCE"))
                        return Mono.just(name); // emite un usuario
                    else
                        return Mono.empty();
                })
                .map(name -> name.toLowerCase())
                .subscribe(u -> log.info(u.toString()));

    }

    public void ejemploFlatMap() throws Exception {
        final List<String> usersList = new ArrayList<>();
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
                    if (user.getName().equalsIgnoreCase("bruce"))
                        return Mono.just(user); // emite un usuario
                    else
                        return Mono.empty();
                })
                .map(user -> {
                    String nameUser = user.getName().toLowerCase();
                    user.setName(nameUser);
                    return user;
                })
                .subscribe(u -> log.info(u.toString()));

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


    private List<User> rellenarUsuarios() {
        List<User> userList = new ArrayList<>();

        userList.add(new User().setName("Andres").setLastName("Guzman"));
        userList.add(new User().setName("Pedro").setLastName("Fernandez"));
        userList.add(new User().setName("Miguel").setLastName("Rguez"));
        userList.add(new User().setName("Diego").setLastName("Farcia"));
        userList.add(new User().setName("Bruce").setLastName("Lee"));
        userList.add(new User().setName("Bruce").setLastName("Guzman"));
        userList.add(new User().setName("Andres").setLastName("Willies"));

        return userList;
    }
}
