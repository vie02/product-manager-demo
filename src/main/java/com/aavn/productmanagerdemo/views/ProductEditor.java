package com.aavn.productmanagerdemo.views;

import com.aavn.productmanagerdemo.model.Product;
import com.aavn.productmanagerdemo.services.ProductService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class ProductEditor extends FormLayout {

    private final ProductService service;

    private Product product;

    TextField name = new TextField("Product name");
    IntegerField quantity = new IntegerField("Quantity");
    DatePicker date = new DatePicker("Date");
    TextArea description = new TextArea("Description");

    Binder<Product> binder = new BeanValidationBinder<>(Product.class);

    Button saveBtn = new Button("Save");
    Button resetBtn = new Button("Reset");
    Button deleteBtn = new Button("Delete");

    SavedCallBack savedCallBack;

    public interface SavedCallBack {
        void call();
    }

    @Autowired
    public ProductEditor(ProductService service) {
        this.service = service;

        configureFields();
        binder.bindInstanceFields(this);
        add(name,
                quantity,
                date,
                description,
                createFormFooter());
    }

    private void configureFields() {
        setColspan(name, 2);
        quantity.setHasControls(true);
        quantity.setMin(0);
        setColspan(quantity, 1);
        setColspan(date, 1);
        setColspan(description, 2);
        description.getStyle().set("minHeight", "120px");
    }

    private Component createFormFooter() {
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
        resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveBtn.addClickListener(e -> save());
        deleteBtn.addClickListener(e -> delete());
        resetBtn.addClickListener(e -> edit(this.product));
        binder.addStatusChangeListener(e -> saveBtn.setEnabled(binder.isValid()));


        HorizontalLayout footer = new HorizontalLayout(
                new HorizontalLayout(saveBtn, resetBtn),
                deleteBtn);

        footer.setHeight("60px");
        footer.setSpacing(false);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        footer.setAlignItems(FlexComponent.Alignment.END);

        setColspan(footer, 2);
        return footer;
    }

    void delete() {
        if (this.product != null && product.getId() != null) {
            service.deleteProduct(this.product);
        }
        savedCallBack.call();
    }

    void save() {
        try {
            binder.writeBean(product);
        } catch (ValidationException e) {
            e.printStackTrace();
            return;
        }
        service.saveProduct(this.product);
        savedCallBack.call();
    }

    public void edit(Product product) {
        if (product == null) {
            return;
        }
        boolean persisted = product.getId() != null;
        if (persisted) {
            // reload data from database
            this.product = service.getProductById(product.getId()).orElse(new Product());
        } else {
            this.product = product;
        }
        resetBtn.setVisible(persisted);
        binder.setBean(this.product);
    }

    public void setSavedCallBack(SavedCallBack savedCallBack) {
        this.savedCallBack = savedCallBack;
    }
}
