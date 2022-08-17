package com.web.client.springclient.dto;

import java.util.Date;

public class Product {

    private String id;
    private String name;
    private Date createAt;
    private Double price;

    private Category category;

    private String photo;

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

    public Date getCreateAt() {
        return createAt;
    }

    public Product setCreateAt(Date createAt) {
        this.createAt = createAt;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public Product setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Product setCategory(Category category) {
        this.category = category;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public Product setPhoto(String photo) {
        this.photo = photo;
        return this;
    }
}
