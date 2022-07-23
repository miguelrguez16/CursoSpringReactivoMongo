package com.inicio.spring.reactivo.InincioSpringReactivo.models;

import java.util.ArrayList;
import java.util.List;

public class Comments {

    private List<String> coments;

    public Comments(){
        this.coments = new ArrayList<>();
    }


    public void addComment(String newComment){
        this.coments.add(newComment);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Comments{");
        sb.append("coments=").append(coments);
        sb.append('}');
        return sb.toString();
    }
}
