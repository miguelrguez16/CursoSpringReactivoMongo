package com.eol.pruebas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController("/")
public class PruebaController {

    @GetMapping("/")
    public String homePage() {

        return "home";
    }

}
