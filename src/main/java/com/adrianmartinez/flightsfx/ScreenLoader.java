package com.adrianmartinez.flightsfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ScreenLoader {

    public static void loadScreen(String viewPath, ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent view = FXMLLoader.load(Objects.requireNonNull(ScreenLoader.class.getResource(viewPath)));
        Scene viewScene = new Scene(view);
        stage.setScene(viewScene);
        stage.show();
    }

    public static void loadScreen(String viewPath, Node node) throws IOException {
        Stage stage = (Stage) (node).getScene().getWindow();
        Parent view = FXMLLoader.load(Objects.requireNonNull(ScreenLoader.class.getResource(viewPath)));
        Scene viewScene = new Scene(view);
        stage.setScene(viewScene);
        stage.show();
    }

}
