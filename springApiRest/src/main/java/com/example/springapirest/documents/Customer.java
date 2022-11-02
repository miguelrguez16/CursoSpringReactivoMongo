package com.example.springapirest.documents;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Document(collection = "customer")
public class Customer {

    @Id
    @NotEmpty
    private String id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastnames;

    private List<Factura> facturaList;

    public Customer() {
    }

    public String getId() {
        return id;
    }

    public Customer setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Customer setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastnames() {
        return lastnames;
    }

    public Customer setLastnames(String lastnames) {
        this.lastnames = lastnames;
        return this;
    }

    public List<Factura> getFacturaList() {
        return facturaList;
    }

    public Customer setFacturaList(List<Factura> facturaList) {
        this.facturaList = facturaList;
        return this;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lastnames='" + lastnames + '\'' +
                ", facturaList=" + facturaList +
                '}';
    }
}
