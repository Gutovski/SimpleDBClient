module com.example.simpledbclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.simpledbclient to javafx.fxml;
    exports com.example.simpledbclient;
}