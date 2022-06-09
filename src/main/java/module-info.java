module com.example.flightsfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.adrianmartinez.flightsfx.model to javafx.base;

    opens com.adrianmartinez.flightsfx to javafx.fxml;
    exports com.adrianmartinez.flightsfx;
}