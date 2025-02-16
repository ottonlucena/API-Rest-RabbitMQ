package com.application.dataService.SpringBootRest.data.service;

import com.application.dataService.SpringBootRest.data.entities.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    List<Product> findAllProducts();

    Optional<Product> findByIdProduct(Long id);

    Product createProduct(Product product);

    Product updateProduct(Long id, Product product);

    void deleteByIdProduct(Long id);

    Product updateStock(Long id, int quantity);
}
