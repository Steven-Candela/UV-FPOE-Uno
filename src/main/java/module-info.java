module com.example.uno {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.uno to javafx.fxml;
    exports com.uno;
    exports com.uno.controlador;
    opens com.uno.controlador to javafx.fxml;
}