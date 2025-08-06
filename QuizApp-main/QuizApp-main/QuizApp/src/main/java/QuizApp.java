package com.example.quizapp;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class QuizApp extends Application {

    private final String DB_URL = "jdbc:mysql://localhost:3306/quizdb";
    private final String DB_USER = "root"; // change to your DB username
    private final String DB_PASS = "8265"; // change to your DB password

    private int score = 0;
    private String studentId;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        askForStudentID(primaryStage);
    }

    private void askForStudentID(Stage stage) {
        Label label = new Label("শিক্ষার্থীর আইডি দিন:");
        TextField idField = new TextField();
        Button nextButton = new Button("সাবমিট করুন");

        VBox layout = new VBox(10, label, idField, nextButton);
        layout.setPadding(new Insets(20));
        Scene scene = new Scene(layout, 300, 200);

        nextButton.setOnAction(e -> {
            studentId = idField.getText().trim();
            if ("IT22040".equals(studentId)) {
                showQuestion1(stage);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "ভুল আইডি!");
                alert.showAndWait();
            }
        });

        stage.setScene(scene);
        stage.setTitle("Quiz Login");
        stage.show();
    }

    private void showQuestion1(Stage stage) {
        Label question = new Label("প্রশ্ন ১: বাংলাদেশ কখন স্বাধীন হয়?");
        RadioButton opt1 = new RadioButton("১৯৭০");
        RadioButton opt2 = new RadioButton("১৯৭১"); // correct
        RadioButton opt3 = new RadioButton("১৯৭২");
        RadioButton opt4 = new RadioButton("১৯৭৩");

        ToggleGroup group = new ToggleGroup();
        opt1.setToggleGroup(group);
        opt2.setToggleGroup(group);
        opt3.setToggleGroup(group);
        opt4.setToggleGroup(group);

        Button next = new Button("পরবর্তী");

        VBox layout = new VBox(10, question, opt1, opt2, opt3, opt4, next);
        layout.setPadding(new Insets(20));
        Scene scene = new Scene(layout, 400, 300);

        next.setOnAction(e -> {
            if (opt2.isSelected()) {
                score += 40;
            }
            showQuestion2(stage);
        });

        stage.setScene(scene);
    }

    private void showQuestion2(Stage stage) {
        Label question = new Label("প্রশ্ন ২: বাংলাদেশের রাজধানী কি?");
        RadioButton opt1 = new RadioButton("বরিশাল");
        RadioButton opt2 = new RadioButton("খুলনা");
        RadioButton opt3 = new RadioButton("রাজশাহী");
        RadioButton opt4 = new RadioButton("ঢাকা"); // correct

        ToggleGroup group = new ToggleGroup();
        opt1.setToggleGroup(group);
        opt2.setToggleGroup(group);
        opt3.setToggleGroup(group);
        opt4.setToggleGroup(group);

        Button submit = new Button("জমা দিন");

        VBox layout = new VBox(10, question, opt1, opt2, opt3, opt4, submit);
        layout.setPadding(new Insets(20));
        Scene scene = new Scene(layout, 400, 300);

        submit.setOnAction(e -> {
            if (opt4.isSelected()) {
                score += 22;
            }
            saveResult();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Quiz শেষ হয়েছে। আপনার স্কোর: " + score);
            alert.showAndWait();
            stage.close();
        });

        stage.setScene(scene);
    }

    private void saveResult() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO quiz_result (student_id, score) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentId);
            stmt.setInt(2, score);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
