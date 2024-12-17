module com.example.weaterapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires json.simple;


    opens com.example.weaterapp to javafx.fxml;
    exports com.example.weaterapp;
}