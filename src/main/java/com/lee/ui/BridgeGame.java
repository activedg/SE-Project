package com.lee.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BridgeGame extends Application {
    // 화면이 보이는 Stage
    public static Stage mainStage;

    // 시작 화면 보여주는 함수
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

    // Stage getter
    protected static Stage getStage(){
        return mainStage;
    }

    public static void main(String[] args) {
        launch();
    }
}