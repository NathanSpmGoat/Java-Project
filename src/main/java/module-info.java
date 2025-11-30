module model.entities {
    requires javafx.controls;
    requires javafx.fxml;


    opens model.entities to javafx.fxml;
    exports model.entities;
}