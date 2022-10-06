package com.inicio.spring.reactivo.iniciospringreactivo.models;

public class User {
    private String name, lastName;

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder user = new StringBuilder();
        user.append(this.getName());
        user.append(" ");
        user.append(this.lastName);
        return user.toString();
    }
}
