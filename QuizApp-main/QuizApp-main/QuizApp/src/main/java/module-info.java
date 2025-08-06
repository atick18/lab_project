module com.example.quizapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;

    opens com.example.quizapp to javafx.fxml;
    exports com.example.quizapp;
}