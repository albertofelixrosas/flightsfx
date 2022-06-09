package com.adrianmartinez.flightsfx;

import com.adrianmartinez.flightsfx.exceptions.InvalidFormatException;
import com.adrianmartinez.flightsfx.exceptions.RequiredFieldEmptyException;
import com.adrianmartinez.flightsfx.global.Formats;
import com.adrianmartinez.flightsfx.model.FlightFilterOption;
import com.adrianmartinez.flightsfx.model.FlightFilters;
import com.adrianmartinez.flightsfx.utils.FileUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import com.adrianmartinez.flightsfx.exceptions.*;
import com.adrianmartinez.flightsfx.model.Flight;

public class FXMLMainViewController implements Initializable {

    @FXML
    private Button buttonAddFlights;

    @FXML
    private Button buttonApplyFilters;

    @FXML
    private Button buttonDeleteFlights;

    @FXML
    private Button buttonModifyFlights;

    @FXML
    private ComboBox<FlightFilterOption> comboBoxFilters;

    @FXML
    private DatePicker datePickerDepartureDate;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem menuItemChart;

    @FXML
    private MenuItem menuItemClose;

    @FXML
    private TableColumn<Flight, LocalDateTime> tableColumnDeparture;

    @FXML
    private TableColumn<Flight, String> tableColumnDestination;

    @FXML
    private TableColumn<Flight, LocalTime> tableColumnDuration;

    @FXML
    private TableColumn<Flight, String> tableColumnFlightNumber;

    @FXML
    private TableView<Flight> tableViewFlights;

    @FXML
    private TextField textFieldDepartureTime;

    @FXML
    private TextField textFieldDestination;

    @FXML
    private TextField textFieldDuration;

    @FXML
    private TextField textFieldFlightNumber;

    private ObservableList<Flight> flights;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<FlightFilterOption> flightFilterOptions = FXCollections.observableArrayList();
        flightFilterOptions.addAll(Arrays.asList(FlightFilterOption.values()));
        comboBoxFilters.setItems(flightFilterOptions);
        comboBoxFilters.getSelectionModel().select(FlightFilterOption.SHOW_ALL);

        refreshTableData();

        tableColumnFlightNumber.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        tableColumnDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
        //tableColumnDeparture.setCellValueFactory(new PropertyValueFactory<>("departure"));
        // getDepartureWithDateFormat
        tableColumnDeparture.setCellValueFactory(new PropertyValueFactory<>("departureWithDateFormat"));
        tableColumnDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
    }

    private void refreshTableData() {
        try {
            flights = FXCollections.observableArrayList(FileUtils.loadFlights());
            tableViewFlights.setItems(flights);
            lockDeleteAndModifyButtons();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buttonApplyFiltersOnAction(ActionEvent event) {
        FlightFilterOption ff = comboBoxFilters.getSelectionModel().getSelectedItem();
        try {
            List<Flight> loadFlights = FileUtils.loadFlights();
            List<Flight> filteredFlights = null;
            switch (ff) {
                case SHOW_ALL:
                    this.flights.clear();
                    filteredFlights = FlightFilters.showAll(loadFlights);
                    this.flights.addAll(filteredFlights);
                    break;
                case SHOW_CURRENTLY_CITY:
                    Flight selectedFlight = tableViewFlights.getSelectionModel().getSelectedItem();
                    if (selectedFlight == null) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "To display flights from the same city, you need to select a flight.");
                        alert.show();
                        return;
                    }
                    String city = selectedFlight.getDestination();
                    this.flights.clear();
                    filteredFlights = FlightFilters.showCurrentlyCity(loadFlights, city);
                    this.flights.addAll(filteredFlights);
                    break;
                case SHOW_FLIGHT_DURATION_AVERAGE:
                    LocalTime timeAverage = FlightFilters.showFlightsAverageDuration(loadFlights);
                    // Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + selection + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "The average flight time is: " + timeAverage.format(Formats.tf));
                    alert.show();
                    break;
                case SHOW_LONG_FLIGHTS:
                    this.flights.clear();
                    filteredFlights = FlightFilters.showLongFlights(loadFlights);
                    this.flights.addAll(filteredFlights);
                    break;
                case SHOW_NEXT_FLIGHTS:
                    this.flights.clear();
                    filteredFlights = FlightFilters.showNextFlights(loadFlights);
                    this.flights.addAll(filteredFlights);
                    break;
            }
            lockDeleteAndModifyButtons();
            cleanFlightDataOnForm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buttonAddFlightsOnAction(ActionEvent event) {
        try {
            Flight flightByUser = getFlightByUser();
            List<Flight> list = FileUtils.loadFlights();
            boolean alreadyExists = list.stream().anyMatch(flight -> flight.getFlightNumber().equalsIgnoreCase(flightByUser.getFlightNumber()));
            if (alreadyExists) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The flight whose flight number is: \"" + flightByUser.getFlightNumber() + "\" already exists\nPerhaps what you were trying to do was modify the registry");
                alert.show();
                return;
            }
            list.add(flightByUser);
            FileUtils.saveFlights(list);
            refreshTableData();
            cleanFlightDataOnForm();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
    }

    private Flight getFlightByUser() throws RequiredFieldEmptyException, InvalidFormatException {
        String flightNumber = textFieldFlightNumber.getText().trim();
        if (flightNumber.isEmpty()) {
            throw new RequiredFieldEmptyException("The \"Flight Number\" es required and it was empty");
        }
        String destination = textFieldDestination.getText().trim();
        if (destination.isEmpty()) {
            throw new RequiredFieldEmptyException("The \"Destination\" es required and it was empty");
        }

        LocalDate dateValue = datePickerDepartureDate.getValue();
        if (dateValue == null) {
            throw new RequiredFieldEmptyException("The \"Departure\" es required and it was empty\nEnter a date value in the form");
        }
        String timeValue = textFieldDepartureTime.getText().trim();
        if (timeValue.isEmpty()) {
            throw new RequiredFieldEmptyException("The \"Departure\" es required and it was empty\nEnter a time value in the form");
        }

        String departureDateStr = "";
        try {
            departureDateStr = dateValue.format(Formats.df);
        } catch (Exception e) {
            throw new InvalidFormatException("The date value of \"Departure\" is not in a valid date format");
        }

        try{
            LocalTime.parse(timeValue);
        } catch (Exception e) {
            throw new InvalidFormatException("The \"Departure\" is not in a valid format in the time value");
        }

        LocalDateTime departure;
        LocalTime duration;
        try {
            departure = LocalDateTime.parse(departureDateStr + ' ' + timeValue, Formats.dtf);
        } catch (Exception e) {
            throw new InvalidFormatException("The \"Departure\" is not in a valid format in the date value");
        }

        String durationStr = textFieldDuration.getText().trim();
        if (durationStr.isEmpty()) {
            throw new RequiredFieldEmptyException("The \"Duration\" es required and it was empty");
        }

        try {
            duration = LocalTime.parse(durationStr, Formats.tf);
        } catch (Exception e) {
            throw new InvalidFormatException("The \"Duration\" is not in a valid format");
        }

        Flight flight = new Flight(flightNumber);
        flight.setDestination(destination);
        flight.setDeparture(departure);
        flight.setDuration(duration);
        return flight;
    }

    @FXML
    void buttonDeleteFlightsOnAction(ActionEvent event) {
        try {
            Flight selectedItem = tableViewFlights.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                return;
            }
            List<Flight> loadFlights = FileUtils.loadFlights();
            List<Flight> resultFlights = loadFlights.stream()
                    .filter(flight -> !flight.getFlightNumber().equalsIgnoreCase(selectedItem.getFlightNumber()))
                    .collect(Collectors.toList());
            FileUtils.saveFlights(resultFlights);
            refreshTableData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buttonModifyFlightsOnAction(ActionEvent event) {
        try {
            Flight flightByUser = getFlightByUser();
            List<Flight> loadFlights = FileUtils.loadFlights();
            boolean exists = false;
            for (Flight flight : loadFlights) {
                if (flight.getFlightNumber().equalsIgnoreCase(flightByUser.getFlightNumber())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The flight with \"" + flightByUser.getFlightNumber() + "\" flight number not exist.");
                alert.show();
                return;
            }
            List<Flight> resultFlights = loadFlights.stream()
                    .map(flight -> flight.getFlightNumber().equals(flightByUser.getFlightNumber()) ? flightByUser : flight)
                    .collect(Collectors.toList());
            FileUtils.saveFlights(resultFlights);
            refreshTableData();
            cleanFlightDataOnForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void menuItemChartOnAction(ActionEvent event) {
        try {
            if(flights.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "To show the graph it is necessary to have at least one flight data.");
                alert.show();
                return;
            }
            ScreenLoader.loadScreen("FXMLChartView.fxml", menuBar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void menuItemCloseOnAction(ActionEvent event) {
        Platform.exit();
    }

    private void cleanFlightDataOnForm() {
        this.textFieldFlightNumber.setText("");
        this.textFieldDestination.setText("");
        this.datePickerDepartureDate.setValue(null);
        this.textFieldDepartureTime.setText("");
        this.textFieldDuration.setText("");
    }

    private void setFlightDataOnForm(Flight flight) {
        this.textFieldFlightNumber.setText(flight.getFlightNumber());
        this.textFieldDestination.setText(flight.getDestination());
        this.datePickerDepartureDate.setValue(flight.getDeparture().toLocalDate());
        this.textFieldDepartureTime.setText(flight.getDeparture().toLocalTime().format(Formats.tf));
        this.textFieldDuration.setText(flight.getDuration().format(Formats.tf));
    }

    private void lockDeleteAndModifyButtons() {
        this.buttonDeleteFlights.setDisable(true);
        this.buttonModifyFlights.setDisable(true);
    }

    private void unlockDeleteAndModifyButtons() {
        this.buttonDeleteFlights.setDisable(false);
        this.buttonModifyFlights.setDisable(false);
    }

    @FXML
    void tableViewFlightsOnMouseClicked(MouseEvent event) {
        Flight flight = this.tableViewFlights.getSelectionModel().getSelectedItem();
        if (flight == null) {
            return;
        }
        unlockDeleteAndModifyButtons();
        setFlightDataOnForm(flight);
    }

}
