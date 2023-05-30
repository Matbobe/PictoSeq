module com.pictoseq {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens com.pictoseq.app to javafx.fxml;
    exports com.pictoseq.app;

    opens com.pictoseq.controllers to javafx.fxml;
    exports com.pictoseq.controllers;

    opens com.pictoseq.models to javafx.fxml;
    exports com.pictoseq.models;
}