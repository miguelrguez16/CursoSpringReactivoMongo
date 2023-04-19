package com.eol.pruebas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PruebasApplicationTests {

    @Test
    void contextLoads() {
        assert("hola").equals("hola");
    }

}
