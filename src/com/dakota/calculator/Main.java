package com.dakota.calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Calculator.fxml"));
        stage.setTitle("CIS_319 Calculator");
        stage.setScene(new Scene(root, 335, 600));
        stage.setMinWidth(325);
        stage.setMinHeight(400);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
