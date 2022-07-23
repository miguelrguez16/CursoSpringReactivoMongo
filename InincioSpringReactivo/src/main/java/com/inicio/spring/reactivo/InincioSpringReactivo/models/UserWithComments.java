package com.inicio.spring.reactivo.InincioSpringReactivo.models;

public class UserWithComments {
    private User userWithComments;
    private Comments comments;

    public UserWithComments(User user, Comments comments) {
        this.comments = comments;
        this.userWithComments = user;
    }

    @Override
    public String toString() {
        return "UserWithComments{" +
                "userWithComments=" + userWithComments +
                ", comments=" + comments +
                '}';
    }
}
