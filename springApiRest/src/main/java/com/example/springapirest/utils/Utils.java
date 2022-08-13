package com.example.springapirest.utils;

import java.util.UUID;

public class Utils {

    public static String customName(String fileName){
        return UUID.randomUUID().toString().concat(fileName).replace(" ", "").replace("//", "").replace(":", "");
    }
}
