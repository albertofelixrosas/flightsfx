package com.adrianmartinez.flightsfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FlightsFX extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FlightsFX.class.getResource("FXMLMainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 550, 510);
        scene.getStylesheets().add(getClass().getResource("/com/adrianmartinez/flightsfx/application.css").toExternalForm());
        stage.setTitle("Flights for everybody!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}