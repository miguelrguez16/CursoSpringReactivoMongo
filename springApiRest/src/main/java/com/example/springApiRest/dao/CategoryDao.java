package com.example.springApiRest.dao;


import com.example.springApiRest.documents.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryDao extends ReactiveMongoRepository<Category, String> {

}
