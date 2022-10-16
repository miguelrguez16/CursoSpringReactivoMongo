package com.example.springapirest.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Document(collection = "factura")
public class Factura {

    @Id
    private String id;

    @NotEmpty
    @DateTimeFormat(pattern = "%d-%m-%Y-%H:%M:%S")
    private Date invoiceDate;

    private String note;

    private List<Product> productList;

    public Factura() {
    }

    public String getId() {
        return id;
    }

    public Factura setId(String id) {
        this.id = id;
        return this;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public Factura setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public String getNote() {
        return note;
    }

    public Factura setNote(String note) {
        this.note = note;
        return this;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public Factura setProductList(List<Product> productList) {
        this.productList = productList;
        return this;
    }

    @Override
    public String toString() {
        return "Factura{" + "id='" + id + '\'' + ", invoiceDate=" + invoiceDate + ", note='" + note + '\'' + ", productList=" + productList + '}';
    }
}
