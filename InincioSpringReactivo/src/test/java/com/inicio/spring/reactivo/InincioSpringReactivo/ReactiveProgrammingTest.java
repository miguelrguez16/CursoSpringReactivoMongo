package com.inicio.spring.reactivo.InincioSpringReactivo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;
import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ReactiveProgrammingTest {

    // test sincrono
    @Test
    void testMapFromStringToBigDecimal(){
        assertEquals(0, BigDecimal.TEN.compareTo(new ReactiveProgramming().mapFromStringToDecimal("10")));
    }


    @Test
    void testMonoNoEmpty(){
        StepVerifier
                .create(new ReactiveProgramming().monoNoEmpty(Mono.empty()))
                .expectNext(0)
                .expectComplete()
                .verify();
    }


    @Test
    void testMonoNoEmptyWithMono(){
        StepVerifier
                .create(new ReactiveProgramming().monoNoEmpty(Mono.just(1)))
                .expectNext(1)
                .expectComplete()
                .verify();
    }

    @Test
    void testMonoFromStringToBigDecimalStepVerifier(){
        StepVerifier
                .create(new ReactiveProgramming().mapFromMonoStringToDecimal(
                        Mono.just("10")
                ))
                .expectNext(BigDecimal.TEN)
                .expectComplete()
                .verify();
    }

    @Test
    void TestMapFluxFromStringToBigDecimalStepVerifier(){
        StepVerifier
                .create(new ReactiveProgramming().fluxIntegerToBigDecimal(
                        Flux.interval(Duration.ofMillis(10)).map(String::valueOf)).take(5)
                )
                .expectNext(BigDecimal.ZERO,BigDecimal.ONE) //entre el primer valor y el segundo son solo 10 ms
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }

    @Test
    void TestFilterPositive(){
        StepVerifier
                .create(new ReactiveProgramming().filterPositive(
                        Flux.just(1,4,-1,8,13)
                ))
                .expectNextCount(2 )
                .expectError()
                .verify();
    }
}
