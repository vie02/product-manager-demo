package com.aavn.productmanagerdemo.services;

import com.aavn.productmanagerdemo.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();

//    List<Product> getProductsByName(String name);

    Optional<Product> getProductById(Long id);

    Product saveProduct(Product product);

    void deleteProduct(Product product);
}
