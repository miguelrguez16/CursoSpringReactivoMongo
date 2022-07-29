package com.spring.reactive.spring.webflux.models.dao;

import com.spring.reactive.spring.webflux.models.documents.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryDao extends ReactiveMongoRepository<Category, String> {

}
