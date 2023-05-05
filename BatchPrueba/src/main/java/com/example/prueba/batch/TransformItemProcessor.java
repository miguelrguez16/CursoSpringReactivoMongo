package com.example.prueba.batch;

import com.example.prueba.model.Coffee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class TransformItemProcessor implements ItemProcessor<Coffee, Coffee> {

    private static final Logger log = LoggerFactory.getLogger(TransformItemProcessor.class);


    @Override
    public Coffee process(Coffee currentCoffee) throws Exception {
        log.info(" Proccessing a new list Coffe " + currentCoffee.toString());
        return new Coffee(currentCoffee.getBrand().toUpperCase(),currentCoffee.getOrigin(),currentCoffee.getCharacteristics());
    }
}
