package com.inicio.spring.reactivo.iniciospringreactivo;

import org.apache.logging.log4j.LogManager;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class ReactiveProgramming {
    /**
     * antigua forma
     *
     * @param String value
     * @return BigDecimal from a String
     */
    public BigDecimal mapFromStringToDecimal(String value) {
        return new BigDecimal(value);
    }

//    public Mono<BigDecimal> mapFromMonoStringToDecimalEmitter(Mono<String> stringMono) {
//        EmitterProcessor<BigDecimal> emitter = EmitterProcessor.create();
//        stringMono.subscribe(s -> emitter.onNext(new BigDecimal(s)
//        );
//        return emitter.next();
//    }

    public Mono<BigDecimal> mapFromMonoStringToDecimal(Mono<String> value) {
        return value.map(BigDecimal::new);
    }

    public Mono<BigDecimal> mapFromMonoStringToDecimalMas099(Mono<String> value) {
        return value.map(s -> new BigDecimal(s + ".99"));
    }


    public Flux<Integer> logs(Flux<Integer> flux) {
        return flux.doOnNext(LogManager.getLogger(this.getClass())::debug);
    }

    public Flux<Integer> filterPositive(Flux<Integer> integerFlux) {
        return integerFlux.handle((n, sink) -> {
            if (n < 0) sink.error(new RuntimeException("-"));
            else sink.next(n);
        });
    }

    public Mono<Integer> monoNoEmpty(Mono<Integer> integerMono) {
        return integerMono.switchIfEmpty(Mono.just(0));

    }

    public Flux<BigDecimal> fluxIntegerToBigDecimal(Flux<String> stringFlux) {
        return stringFlux.map(integer -> new BigDecimal(String.valueOf(integer)));
    }

    public Flux<Integer> convertToInteger(Flux<String> stringFlux) {
        return stringFlux.map(Integer::valueOf); //Integer.valueOf/stringFlux
    }

    public Flux<Integer> convertToIntegerFlapMap(Flux<String> stringFlux) {
        return stringFlux.flatMap(s -> Mono.just(Integer.valueOf(s)));
        // con mono just emitimos un flujo de un solo dato
        // con el flatmap lo convierte en un solo flujo de un monton de datos
    }
}
