module Weather {
    requires javafx.controls;
    requires javafx.fxml;
    exports Weather;
    opens Weather.controller;
}