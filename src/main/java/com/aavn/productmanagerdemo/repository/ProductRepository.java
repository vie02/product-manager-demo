package com.aavn.productmanagerdemo.repository;

import com.aavn.productmanagerdemo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

//    List<Product> findByNameStartsWithIgnoreCase(String name);
    List<Product> findAllByOrderByIdDesc();
}
