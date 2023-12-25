module com.example.osa {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;



    opens com.example.osa to javafx.fxml;
    exports com.example.osa;
}