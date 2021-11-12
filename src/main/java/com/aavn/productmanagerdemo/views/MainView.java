package com.aavn.productmanagerdemo.views;

import com.aavn.productmanagerdemo.model.Product;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Push
@Route
public class MainView extends VerticalLayout {

    final TextField filter;
    final Button addNewBtn;
    final Grid<Product> grid;
    final ProductDialog dialog;
    final ProductDataProvider dataProvider;

    @Autowired
    public MainView(ProductDataProvider dataProvider, ProductDialog dialog) {
        this.grid = new Grid<>(Product.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Add product", VaadinIcon.PLUS.create());
        this.dialog = dialog;
        this.dataProvider = dataProvider;

        configureGrid();
        configureDialog();

        setAlignItems(Alignment.CENTER);

        add(createToolBar(), this.grid);
    }

    private void configureDialog() {
        dialog.setMaxWidth("800px");
        dialog.setEditorCallBack(() -> {
            dialog.close();
            dataProvider.
                    refresh();
        });
        dialog.addDialogCloseActionListener(e -> {
            grid.select(null);
            dialog.close();
        });
    }

    private HorizontalLayout createToolBar() {
        filter.setWidth("300px");
        filter.setPlaceholder("Filter by keyword");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> filterProducts(e.getValue()));

        addNewBtn.addClickListener(e -> {
            dialog.edit(new Product("", 0, LocalDate.now(), ""));
        });
        return new HorizontalLayout(filter, addNewBtn);
    }

    private void configureGrid() {
        grid.setMaxWidth("1000px");
        grid.setWidthFull();
        grid.setHeight("800px");
        grid.setColumns("name", "quantity", "date", "description");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setDataProvider(this.dataProvider);

        grid.asSingleSelect().addValueChangeListener(e -> {
            dialog.edit(e.getValue());
        });
        setHorizontalComponentAlignment(Alignment.CENTER, grid);
    }

    private void filterProducts(String keyword) {
        dataProvider.setFilter(product -> {
            if (StringUtils.isEmpty(keyword.trim())) {
                return true;
            }
            String text = keyword.trim().toLowerCase();
            return product.getName().toLowerCase().contains(text)
                    || product.getDescription().toLowerCase().contains(text)
                    || product.getDate().toString().toLowerCase().contains(text);
        });
    }
}
