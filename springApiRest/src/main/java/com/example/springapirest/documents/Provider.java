package com.example.springapirest.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Document(collection = "provider")
public class Provider {

    @Id
    @NotEmpty
    private String id;
    @NotEmpty
    private String company;
    @NotEmpty
    private String phone;
    @NotEmpty
    private String note;

    public Provider() { }


    /* Get and set */
    public String getId() {
        return id;
    }

    public Provider setId(String id) {
        this.id = id;
        return this;
    }

    public String getCompany() {
        return company;
    }

    public Provider setCompany(String company) {
        this.company = company;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Provider setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getNote() {
        return note;
    }

    public Provider setNote(String note) {
        this.note = note;
        return this;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "id='" + id + '\'' +
                ", company='" + company + '\'' +
                ", phone='" + phone + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
