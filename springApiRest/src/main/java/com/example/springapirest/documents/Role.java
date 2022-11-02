package com.example.springapirest.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Document(collection = "role")
public class Role implements GrantedAuthority {

    @Id
    private String id;

    private String type;

    public Role() {
    }

    public String getId() {
        return id;
    }

    public Role setId(String id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public Role setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String getAuthority() {
        return type;
    }


    @Override
    public String toString() {
        return "Role{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}