package com.aavn.productmanagerdemo.views;

import com.aavn.productmanagerdemo.model.Product;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class ProductDialog extends Dialog {

    private final ProductEditor editor;

    @Autowired
    public ProductDialog(ProductEditor editor) {
        super();
        this.editor = editor;

        setCloseOnOutsideClick(false);
        add(createControls(), editor);
    }

    private Component createControls() {
        Button close = new Button(VaadinIcon.CLOSE.create());
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        close.addClickListener(e -> this.fireEvent(new DialogCloseActionEvent(this, false)));

        HorizontalLayout controls = new HorizontalLayout(
                new H3("Product Information"),
                close);
        controls.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        controls.setAlignItems(FlexComponent.Alignment.START);
        controls.setSpacing(false);
        return controls;
    }

    public void setEditorCallBack(ProductEditor.SavedCallBack editorCallBack) {
        editor.setSavedCallBack(editorCallBack);
    }

    public void edit(Product product) {
        open();
        editor.edit(product);
    }
}
