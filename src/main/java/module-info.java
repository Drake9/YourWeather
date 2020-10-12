module Weather {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires json;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    exports Weather;
    exports Weather.model;
    opens Weather.controller;
}