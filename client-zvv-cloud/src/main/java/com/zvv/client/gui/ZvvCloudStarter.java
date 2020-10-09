package com.zvv.client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ZvvCloudStarter extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent loginView = FXMLLoader.load(getClass().getResource("/loginView.fxml"));
        primaryStage.setTitle("ZVV Cloud");
        primaryStage.setScene(new Scene(loginView, 300, 275));
        primaryStage.show();
    }

}
