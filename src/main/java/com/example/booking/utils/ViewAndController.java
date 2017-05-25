package com.example.booking.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class ViewAndController<V extends Parent, C> {

    private V parentView;
    private C controller;

    public ViewAndController(V parentView, C controller) {
        this.parentView = parentView;
        this.controller = controller;
    }

    public V getParentView() {
        return parentView;
    }

    public void setParentView(V parentView) {
        this.parentView = parentView;
    }

    public C getController() {
        return controller;
    }

    public void setController(C controller) {
        this.controller = controller;
    }

    public static <V extends Parent, C> ViewAndController<V, C> load(String name) throws IOException {
        try (InputStream fxmlStream = ViewAndController.class.getClassLoader().getResourceAsStream("fxml/" + name + ".fxml")) {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(ResourceBundle.getBundle(name));
            loader.load(fxmlStream);
            return new ViewAndController(loader.getRoot(), loader.getController());
        }
    }
}
