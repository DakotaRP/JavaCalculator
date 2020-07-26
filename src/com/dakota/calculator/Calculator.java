package com.dakota.calculator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Calculator extends Application {

    CalculatorController controller = new CalculatorController();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Calculator.fxml"));

        primaryStage.setTitle("CIS_319 Calculator");
        primaryStage.setScene(new Scene(root, 335, 600));
        primaryStage.setMinWidth(325);
        primaryStage.setMinHeight(400);
        primaryStage.show();
//        controller.readFromFile(); // read from stored file (if any)
//        controller.initializeProgram(); // setup outputs on calculator
//
//        // when program is closed, write all data to a file
//        primaryStage.setOnCloseRequest(event -> {
//            try {
//                controller.writeToFile(); // write variables to file
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Platform.exit(); // gui thread
//            System.exit(0); // normal exit, killing JVM
//        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
