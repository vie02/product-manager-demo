package com.aavn.productmanagerdemo.views;


import com.aavn.productmanagerdemo.model.Product;
import com.aavn.productmanagerdemo.services.ProductService;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
public class ProductDataProvider extends ListDataProvider<Product> {

    private final ProductService service;

    @Autowired
    public ProductDataProvider(ProductService service) {
        super(service.getAllProducts());
        this.service = service;
    }

    public synchronized void refresh() {
        getItems().clear();
        getItems().addAll(service.getAllProducts());
        refreshAll();

    }
}
