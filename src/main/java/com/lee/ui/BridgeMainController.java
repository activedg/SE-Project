package com.lee.ui;

import com.lee.model.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

public class BridgeMainController implements Initializable {
    private static int playerNum;

    private Player curPlayer = null;
    private int moveCount;


    @FXML private Label mainActionLabel;

    @FXML private Button playerNumBtn;

    @FXML private TextField playerNumTF;

    @FXML private Button settingBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerNumBtn.setOnAction((ActionEvent -> {
            playerNumCheck();
        }));

        playerNumTF.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)){
                playerNumCheck();
            }
        });
        settingBtn.setOnAction((ActionEvent -> {
            loadSetting();
        }));
    }

    private void playerNumCheck() {
        String strNum = playerNumTF.getText();
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

            // Set Number of Players
            playerNum = Integer.parseInt(strNum);

            try{
                StackPane stackPane = (StackPane) playerNumBtn.getScene().getRoot();
                Stage primaryStage = BridgeGame.getStage();
                primaryStage.setX(500);
                primaryStage.setY(50);
                primaryStage.setWidth(1200);
                primaryStage.setHeight(900);
                BridgeMapView bridgeMapView = new BridgeMapView();
                bridgeMapView.setMap();
                stackPane.getChildren().remove(0);
                stackPane.getChildren().add(bridgeMapView.getContentPane());

                playBridgeGame(bridgeMapView);
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
    public static int getPlayerNum() {return playerNum;}

    private void loadSetting(){
        StackPane stackPane = (StackPane) settingBtn.getScene().getRoot();

    }

    private void playBridgeGame(BridgeMapView bridgeMapView){
        bridgeMapView.setMyClickListener(new BridgeMapView.onItemClick() {
            @Override
            public void onRollDiceClick(Button diceButton, Button restButton) {
                diceButton.setDisable(true);
                restButton.setDisable(true);
                // Initialize curPlayer
                curPlayer = bridgeMapView.getCurrentPlayer();

                Pane root = (Pane) bridgeMapView.getContentPane();
                StackPane dicePane = new StackPane();
                dicePane.setPrefSize(200, 250);
                dicePane.relocate(root.getPrefWidth()-300, 500);

                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(200);
                rectangle.setHeight(300);
                rectangle.setFill(Color.GOLD);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(2);

                File file = new File("src/main/resources/com/lee/image/dice1.png");
                ImageView diceImage= new ImageView(new Image(file.toURI().toString()));
                diceImage.setFitHeight(100);
                diceImage.setFitWidth(100);
                diceImage.setPreserveRatio(true);

                StackPane.setAlignment(diceImage, Pos.TOP_LEFT);
                StackPane.setMargin(diceImage, new Insets(20, 0, 0, 20));

                Button rollButton = new Button("Roll");
                StackPane.setAlignment(rollButton, Pos.TOP_RIGHT);
                StackPane.setMargin(rollButton, new Insets(60, 20 , 0, 0));

                TextField moveTF = new TextField();
                moveTF.setPrefSize(150, 30);
                moveTF.setMaxWidth(180);
                moveTF.setFont(Font.font("Arial", 14));
                moveTF.setPromptText("Combinations of U,D,L,R");
                moveTF.setFocusTraversable(false);
                moveTF.setDisable(true);
                StackPane.setAlignment(moveTF, Pos.CENTER);
                StackPane.setMargin(moveTF, new Insets(40, 0, 0, 0));

                Button exitButton = new Button("Exit");
                exitButton.setDisable(true);
                StackPane.setAlignment(exitButton, Pos.BOTTOM_CENTER);
                StackPane.setMargin(exitButton, new Insets(0, 0, 20, 0));

                dicePane.getChildren().addAll(rectangle, diceImage, rollButton, moveTF, exitButton);
                root.getChildren().add(dicePane);

                rollButton.setOnAction(actionEvent -> {
                    roll(diceImage, rollButton, exitButton , moveTF, bridgeMapView);
                });

                exitButton.setOnAction(actionEvent -> {
                    root.getChildren().remove(dicePane);
                    diceButton.setDisable(false);
                    exitButton.setDisable(false);
                    bridgeMapView.changeTurn(restButton);
                });


                moveTF.setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode().equals(KeyCode.ENTER)){
                        String moveStr = moveTF.getText().toString().toUpperCase(Locale.ROOT);
                        if (moveStr.isEmpty()){
                            moveTF.setPromptText("Combinations of U,D,L,R");
                        }
                        else {
                            moveStr = moveStr.replaceAll(" ", "");
                            if (moveStr.length() != moveCount) {
                                moveTF.setText("Length should be " + moveCount);
                                // Should Implement
                            }
                            else if (bridgeMapView.checkMoveInfo(moveStr) == false){
                                moveTF.setText("Invalid Move. Input Again");
                                System.out.println("Current Pos : " + bridgeMapView.getCurrentPlayer().getXPos() + ", "  + bridgeMapView.getCurrentPlayer().getYPos());
                            } else{
                                bridgeMapView.move(moveStr, exitButton);
                                moveTF.setDisable(true);
                            }
                        }
                    }
                });
            }

            @Override
            public void onRestClick() {
                Player curPlayer = bridgeMapView.getCurrentPlayer();
                curPlayer.rest();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bridgeMapView.updatePlayerCardInfo("Bridge", 1);
            }
        });
    }

    private void roll(ImageView imageView, Button rollButton, Button exitButton, TextField moveTF, BridgeMapView bridgeMapView){
        Random random = new Random();
        final int[] randomNum = {0};

        Thread thread = new Thread(){
            public void run() {
                try {
                    rollButton.setDisable(true);
                    for (int i = 0; i < 15; i++) {
                        randomNum[0] = (random.nextInt(6) + 1);
                        File file = new File("src/main/resources/com/lee/image/dice" + randomNum[0] + ".png");
                        imageView.setImage(new Image(file.toURI().toString()));
                        imageView.setFitHeight(100);
                        imageView.setFitWidth(100);
                        imageView.setPreserveRatio(true);
                        Thread.sleep(50);
                    }
                    moveTF.setDisable(false);
                    moveCount = randomNum[0] - curPlayer.getBridgeCardNum();
                    // 이동 가능한 move가 0번일 때
                    if (moveCount <= 0 ) {
                        moveCount = 0;
                        moveTF.setDisable(true);
                        exitButton.setDisable(false);
                        bridgeMapView.getCurrentPlayer().rest();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                bridgeMapView.updatePlayerCardInfo("Bridge", 1);
                            }
                        });
                    }
                    else moveTF.setPromptText("   " + moveCount + " moves possible");

                    this.interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }
}