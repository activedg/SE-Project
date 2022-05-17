package com.lee.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BridgeMainController implements Initializable {

    @FXML private Label mainActionLabel;

    @FXML private Button playerNumBtn;

    @FXML private TextField playerNum;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerNumBtn.setOnAction((ActionEvent -> {
            playerNumCheck();
        }));

        playerNum.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)){
                playerNumCheck();
            }
        });
    }

    private void playerNumCheck() {
        String strNum = playerNum.getText();
        if (strNum.isEmpty()) {
            mainActionLabel.setText("숫자를 입력해 주세요. (2 ~ 4)");
            return;
        }
        else{
            for (int i=0; i<strNum.length(); i++){
                if (Character.isDigit(strNum.charAt(i)) == false){
                    mainActionLabel.setText("숫자를 입력해 주세요. (2 ~ 4)");
                    return;
                }
            }
            if (Integer.parseInt(strNum) < 2 || Integer.parseInt(strNum) > 4){
                mainActionLabel.setText("잘못된 숫자 범위입니다. 숫자를 입력해주세요. (2 ~ 4)");
                return;
            }

            try{
                StackPane stackPane = (StackPane) playerNumBtn.getScene().getRoot();
                Stage primaryStage = BridgeGame.getStage();
                primaryStage.setX(500);
                primaryStage.setY(50);
                primaryStage.setWidth(1000);
                primaryStage.setHeight(800);
                stackPane.getChildren().add(new BridgeMapView().getContentPane());
//
//                Stage primaryStage = BridgeGame.getStage();
//                Scene scene = new Scene(new BridgeMapView().getContentPane());
//                primaryStage.setScene(scene);
//                primaryStage.setX(500);
//                primaryStage.setY(50);
//                primaryStage.show();

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}