package com.lee.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BridgeGame extends Application {
    private static Stage mainStage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BridgeGame.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        mainStage = stage;
        stage.setTitle("Bridge Game");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    protected static Stage getStage(){
        return mainStage;
    }

    public static void main(String[] args) {
        launch();
    }
}