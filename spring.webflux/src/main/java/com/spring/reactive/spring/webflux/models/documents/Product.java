package com.spring.reactive.spring.webflux.models.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "products")
public class Product {
    @Id
    private String id;

    public Product() {
    }

    private String name;
    private Double price;
    private Date createAt;

    public String getId() {
        return id;
    }

    public Product setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public Product setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Product setCreateAt(Date createAt) {
        this.createAt = createAt;
        return this;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", createAt=" + createAt +
                '}';
    }
}
