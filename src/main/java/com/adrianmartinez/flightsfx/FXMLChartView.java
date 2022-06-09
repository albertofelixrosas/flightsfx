package com.adrianmartinez.flightsfx;

import com.adrianmartinez.flightsfx.model.Flight;
import com.adrianmartinez.flightsfx.utils.FileUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FXMLChartView implements Initializable {

    @FXML
    private Button buttonBack;

    @FXML
    private PieChart pieChartFlightsByDestination;

    @FXML
    void buttonBackOnAction(ActionEvent event) {
        try {
            ScreenLoader.loadScreen("FXMLMainView.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<Flight> flights = FileUtils.loadFlights();
            pieChartFlightsByDestination.getData().clear();
            List<String> destinations = flights.stream()
                    .map(Flight::getDestination)
                    .collect(Collectors.toList());
            Map<String, Long> counted = destinations.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            counted.forEach((destination, sum) -> {
                pieChartFlightsByDestination.getData().add(new PieChart.Data(destination, sum));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
