package com.example.springapirest.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document(collection = "user")
public class User implements UserDetails {
    @Id
    private String id;
    private String completeName;
    private String username;
    private String password;
    private Boolean isActive = true;

    private List<Role> roles = new ArrayList<>();


    public User() {
    }

    public String getId(){
        return id;
    }

    public String getCompleteName() {
        return completeName;
    }

    public User setCompleteName(String completeName) {
        this.completeName = completeName;
        return this;
    }
    public User addRole(Role newRole){
        roles.add(newRole);
        return this;
    }
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", completeName='" + completeName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                ", roles=" + roles +
                '}';
    }
}
