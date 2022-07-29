package com.spring.reactive.spring.webflux.services;

import com.spring.reactive.spring.webflux.models.dao.CategoryDao;
import com.spring.reactive.spring.webflux.models.dao.ProductsDao;
import com.spring.reactive.spring.webflux.models.documents.Category;
import com.spring.reactive.spring.webflux.models.documents.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductsDao productsDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public Flux<Product> findAllProducts() {
        return productsDao.findAll();
    }

    @Override
    public Flux<Category> findAllCategories() {
        return categoryDao.findAll();
    }

    @Override
    public Flux<Product> findAllProductWithNameUpperCase() {
        return productsDao.findAll()
                .map(product ->
                        product.setName(product.getName().toUpperCase()));
    }
    @Override
    public Mono<Product> findProductById(String id) {
        return productsDao.findById(id);
    }
    @Override
    public Mono<Product> saveProduct(Product product) {
        return productsDao.save(product);
//        return product.getId()== null ? productsDao.save(product) : productsDao.insert(product);
    }

    @Override
    public Mono<Void> deleteProduct(Product product) {
        return productsDao.delete(product);
    }

    @Override
    public Mono<Category> findCategoryById(String id) {
        return categoryDao.findById(id);
    }

    @Override
    public Mono<Category> saveCategory(Category category) {
        return categoryDao.save(category);
    }

    @Override
    public Mono<Void> deleteCategory(Category category) {
        return categoryDao.delete(category);
    }

}
