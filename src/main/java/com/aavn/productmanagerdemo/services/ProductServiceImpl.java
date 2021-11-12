package com.aavn.productmanagerdemo.services;

import com.aavn.productmanagerdemo.model.Product;
import com.aavn.productmanagerdemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Autowired
    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.findAllByOrderByIdDesc();
    }

//    @Cacheable(value = "products.byId", key = "#id")
    @Override
    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }

//    @CachePut(value = "products.byId", key = "#product.id")
    @Override
    public Product saveProduct(Product product) {
        return repository.save(product);
    }

//    @CacheEvict(value = "products.byId", key = "#product.id")
    @Override
    public void deleteProduct(Product product) {
        repository.delete(product);
    }
}
