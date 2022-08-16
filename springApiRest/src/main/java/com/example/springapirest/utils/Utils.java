package com.example.springapirest.utils;

import java.util.UUID;

public class Utils {
    public static final String DEFAULT_URI = "/api/v2/products";
    public static final String DEFAULT_URI_ID = "/api/v2/products/{id}";
    public static String customName(String fileName){
        return UUID.randomUUID().toString().concat(fileName).replace(" ", "").replace("//", "").replace(":", "");
    }
}
