module model.entities {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens model.entities to javafx.fxml;
    exports model.entities;
}