package com.lee.ui;

import com.lee.model.BridgeMap;
import com.lee.model.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

public class BridgeMainController implements Initializable {
    // 게임 플레이어 수
    private static int playerNum;
    // 현재 플레이어
    private Player curPlayer = null;
    // 움직일 수 있는 칸 수
    private int moveCount;

    // 주사위 사진 및 버튼, 라벨
    private ImageView diceImage;
    private Button rollButton;
    private Label rollLabel;
    private TextField moveTF;
    private Button exitButton;

    // 시작 화면 뷰들
    @FXML private Label mainActionLabel;
    @FXML private Button playerNumBtn;
    @FXML private TextField playerNumTF;
    @FXML private Button fileBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerNumBtn.setOnAction((ActionEvent -> {
            gameInit();
        }));

        playerNumTF.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)){
                gameInit();
            }
        });
        fileBtn.setOnAction((ActionEvent -> {
            loadFile();
        }));
    }

    // 시작 화면을 설정하는 함수
    private void gameInit() {
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

            // 플레이어 수 설정
            playerNum = Integer.parseInt(strNum);

            try{
                // 새로운 화면 만들기
                StackPane stackPane = (StackPane) playerNumBtn.getScene().getRoot();
                Stage primaryStage = BridgeGame.getStage();
                primaryStage.setX(500);
                primaryStage.setY(50);
                primaryStage.setWidth(1250);
                primaryStage.setHeight(900);
                BridgeMapView bridgeMapView = new BridgeMapView();
                stackPane.getChildren().remove(0);
                stackPane.getChildren().add(bridgeMapView.getScrollPane());

                playBridgeGame(bridgeMapView);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    // 플레이어 숫자 getter
    public static int getPlayerNum() {return playerNum;}

    // 파일 설정 버튼을 클릭한 경우
    private void loadFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Map 파일(*.map)", "*.map"));
        File file = fileChooser.showOpenDialog(BridgeGame.getStage());

        if (file != null) {
            ArrayList<String> mapData = new ArrayList<>();
            BufferedReader br;
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line;
                while ((line = br.readLine()) != null) {
                    mapData.add(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BridgeMap.getInstance().setMapData(mapData.toArray(new String[mapData.size()]));
            mainActionLabel.setText("\t    맵 데이터가 변경되었습니다. \n플레이 할 플레이어 수를 입력하세요. (2~4)");
        }
        else mainActionLabel.setText("플레이 할 플레이어 수를 입력하세요. (2~4)");

    }

    // BridgeGame 게임 화면 내부의 버튼들의 클릭과 인풋 입력을 관리하는 함수
    private void playBridgeGame(BridgeMapView bridgeMapView){
        bridgeMapView.setMyClickListener(new BridgeMapView.onItemClick() {
            @Override
            public void onRollDiceClick(Button diceButton, Button restButton) {
                // 다른 버튼들 비활성화
                diceButton.setDisable(true);
                restButton.setDisable(true);
                // 현재 플레이어
                curPlayer = bridgeMapView.getCurrentPlayer();

                // 주사위 화면을 만들고 보여준다
                Pane root = (Pane) bridgeMapView.getContentPane();
                StackPane dicePane = new StackPane();
                dicePane.setPrefSize(350, 230);
                dicePane.relocate(820, 200);

                Rectangle rectangle = new Rectangle(350, 230);
                rectangle.setFill(Color.WHITE);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(2);

                File file = new File("src/main/resources/com/lee/image/dice1.png");
                diceImage= new ImageView(new Image(file.toURI().toString()));
                diceImage.setFitHeight(100);
                diceImage.setFitWidth(100);
                diceImage.setPreserveRatio(true);
                StackPane.setAlignment(diceImage, Pos.TOP_LEFT);
                StackPane.setMargin(diceImage, new Insets(20, 0, 0, 10));

                rollButton = new Button("Roll");
                rollButton.setPrefSize(50, 30);
                StackPane.setAlignment(rollButton, Pos.BOTTOM_LEFT);
                StackPane.setMargin(rollButton, new Insets(0, 0 , 50, 35));

                rollLabel = new Label("Click Roll button to \nroll a dice.");
                rollLabel.setPrefSize(220, 100);
                rollLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 17));
                StackPane.setAlignment(rollLabel, Pos.TOP_CENTER);
                StackPane.setMargin(rollLabel, new Insets(0, 0, 0 , 120));

                moveTF = new TextField();
                moveTF.setPrefSize(180, 30);
                moveTF.setMaxWidth(180);
                moveTF.setFont(Font.font("Arial", 13));
                moveTF.setPromptText("Combinations of U,D,L,R");
                moveTF.setFocusTraversable(false);
                moveTF.setDisable(true);
                StackPane.setAlignment(moveTF, Pos.CENTER_RIGHT);
                StackPane.setMargin(moveTF, new Insets(0, 45, 20, 0));

                exitButton = new Button("Exit");
                exitButton.setPrefSize(50, 30);
                exitButton.setDisable(true);
                StackPane.setAlignment(exitButton, Pos.BOTTOM_RIGHT);
                StackPane.setMargin(exitButton, new Insets(0, 35, 50, 0));

                dicePane.getChildren().addAll(rectangle, diceImage, rollButton, rollLabel, moveTF, exitButton);
                root.getChildren().add(dicePane);

                // roll 버튼
                rollButton.setOnAction(actionEvent -> {
                    roll(bridgeMapView);
                });

                // 주사위 화면 종료 버튼
                exitButton.setOnAction(actionEvent -> {
                    root.getChildren().remove(dicePane);
                    diceButton.setDisable(false);
                    exitButton.setDisable(false);
                    bridgeMapView.changeTurn(restButton);
                });

                // 주사위 굴린 후 움직일 이동을 입력한 경우
                moveTF.setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode().equals(KeyCode.ENTER)){
                        String moveStr = moveTF.getText().toUpperCase(Locale.ROOT);
                        if (moveStr.isEmpty()){
                            rollLabel.setText("Length should be "+moveCount +".\nInput combinations of \nU, D, L, R or u, d, l, r.");
                        }
                        else {
                            // 인풋값의 유효성 검사
                            moveStr = moveStr.replaceAll(" ", "");
                            if (moveStr.length() != moveCount) {
                                rollLabel.setText("Length should be "+moveCount +".\nInput combinations of \nU, D, L, R or u, d, l, r.");
                            }
                            else if (bridgeMapView.checkMoveInfo(moveStr) == false){
                                rollLabel.setText("Invalid move!! Input again.\nLength should be "+moveCount +".");
                            } else{
                                // 유효한 경우
                                rollLabel.setText("Player is moving..");
                                bridgeMapView.move(moveStr, exitButton, rollLabel);
                                moveTF.setDisable(true);
                            }
                        }
                    }
                });
            }

            @Override
            public void onRestClick() {
                // rest 버튼 클릭시
                Player curPlayer = bridgeMapView.getCurrentPlayer();
                curPlayer.rest();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bridgeMapView.updatePlayerCardInfo("Bridge", 1);
            }

            @Override
            public void onExitClick() {
                // 게임 종료 버튼 클릭시
                System.exit(1);
            }
        });
    }

    // 주사위를 굴리는 스레드를 생성 후 주사위 결과를 보여주는 함수
    private void roll(BridgeMapView bridgeMapView){
        Random random = new Random();
        final int[] randomNum = {0};

        Thread thread = new Thread(){
            public void run() {
                try {
                    rollButton.setDisable(true);
                    for (int i = 0; i < 15; i++) {
                        randomNum[0] = (random.nextInt(6) + 1);
                        File file = new File("src/main/resources/com/lee/image/dice" + randomNum[0] + ".png");
                        diceImage.setImage(new Image(file.toURI().toString()));
                        diceImage.setFitHeight(100);
                        diceImage.setFitWidth(100);
                        diceImage.setPreserveRatio(true);
                        Thread.sleep(50);
                    }
                    moveCount = randomNum[0] - curPlayer.getBridgeCardNum();
                    setViewsAfterRoll(bridgeMapView);
                    this.interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    // 주사위를 굴린 후 텍스트 필드 및 라벨(얼마나 움직일 수 있는지 보여주는 라벨) 설정
    private void setViewsAfterRoll(BridgeMapView bridgeMapView){
        moveTF.setDisable(false);
        // 이동 가능한 move가 0번일 때
        if (moveCount <= 0 ) {
            moveCount = 0;
            moveTF.setDisable(true);
            exitButton.setDisable(false);
            bridgeMapView.getCurrentPlayer().rest();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    rollLabel.setText("Cannot move. Rest\nusing one bridge card.");
                    bridgeMapView.updatePlayerCardInfo("Bridge", 1);
                }
            });
        }
        else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    rollLabel.setText("You can move "+moveCount + " moves.\n" + "Input combinations of \nU, D, L, R or u, d, l, r.");
                    moveTF.setPromptText("   " + moveCount + " moves possible");
                }
            });
        }

    }
}