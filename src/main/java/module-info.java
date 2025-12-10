module model.entities {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;
    requires itextpdf;

    opens model.entities to javafx.fxml;
    opens controller to javafx.fxml;

    exports model.entities;
    exports app;
}
