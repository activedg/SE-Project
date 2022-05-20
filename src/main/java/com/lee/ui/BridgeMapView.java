package com.lee.ui;

import com.lee.model.BridgeMap;
import com.lee.model.Player;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;

public class BridgeMapView {
    public static final int TILE_SIZE = 36;
    public static final int WIDTH = 25;
    public static final int HEIGHT = 25;

    public static final int START_X = 3;
    public static final int START_Y = 8;

    public static final Color[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.PURPLE};

    private int x = START_X, y= START_Y;
    private int endCount = 0;
    private Pane root;
    private Group tileGroup = new Group();
    private BridgeMap map;

    private Tile[][] tiles = new Tile[WIDTH + 1][HEIGHT + 1];
    private int playerNum = 0;
    private int turn = 1;
    private PlayerView[] playerViews;
    private Label[][] playerCards;
    private Label turnLabel;

    public BridgeMapView(){
        map = BridgeMap.getInstance();
        playerNum = BridgeMainController.getPlayerNum();
        playerViews = new PlayerView[playerNum];

        root = new Pane();
        root.setPrefSize(WIDTH * TILE_SIZE + 200, HEIGHT * TILE_SIZE + 200);
        root.getChildren().add(tileGroup);
    }

    private onItemClick myItemClick;
    public interface onItemClick {
        void onRollDiceClick(Button diceButton, Button restButton);
        void onRestClick();
    }
    public void setMyClickListener(onItemClick myItemClick){
        this.myItemClick = myItemClick;
    }

    public Parent getContentPane(){
        return root;
    }
    public Player getCurrentPlayer(){
        return playerViews[turn-1].getPlayer();
    }
    public int getEndCount() {return endCount; }

    public void setMap(){
        // 나중에 지워야 함
        map.defaultMap();
        String mapData[] = map.getMapData();
        if (mapData == null){
            throw new IllegalArgumentException("map 데이터가 null 일 수 없습니다.");
        }

        for (int i = 0; i < mapData.length; i++) {
            String[] temp = mapData[i].split(" ");
            Tile tile;
            if (i == 0 && temp[0].equals("S")) {
                tile = new Tile("START", x, y, null, temp[1]);
                // tiles[x][y] = tile;
                switch (temp[1]) {
                    case "R":
                        tiles[x+1][y] = tile;
                        x += 2;
                        break;
                    case "D":
                        tiles[x][y+1] = tile;
                        y += 2;
                        break;
                }
                initPlayerPos(temp[1]);
            } else if (i == mapData.length - 1 && temp[0].equals("E")) {
                tile = new Tile("END", x, y, null, null);
                tiles[x][y] = tile;
            } else if (temp[0].equals("S")){
                tile = new Tile("SAW", x ,y, temp[1], temp[2]);
                tiles[x][y] = tile;
                setNextXY(temp[2]);
            } else if (temp[0].equals("P")) {
                tile = new Tile("PHILIPS", x, y, temp[1], temp[2]);
                tiles[x][y] = tile;
                setNextXY(temp[2]);
            } else if (temp[0].equals("H")){
                tile = new Tile("HAMMER", x, y, temp[1], temp[2]);
                tiles[x][y] = tile;
                setNextXY(temp[2]);
            } else if (temp[0].equals("B")){
                tile = new Tile("INTERSECT", x ,y, temp[1], temp[2]);
                tiles[x][y] = tile;
                Tile bridge = new Tile("BRIDGE", x+1, y, "L", "R");
                tiles[x+1][y] = bridge;
                tileGroup.getChildren().add(bridge);
                setNextXY(temp[2]);
            }
            else {
                tile = new Tile("CELL", x, y, temp[1], temp[2]);
                tiles[x][y] = tile;
                setNextXY(temp[2]);
            }
            tileGroup.getChildren().add(tile);
        }

        initPlayerInfo();
        initTurnInfo();
    }

    private void setNextXY(String t){
        switch (t) {
            case "R":
                x++;
                break;
            case "D":
                y++;
                break;
            case "U":
                y--;
                break;
        }
    }

    private void initPlayerPos(String s){
        int temp_x = START_X;
        int temp_y = START_Y;

        // 임시
        int temp_margin = 4;

        if (s.equals("R"))
            temp_x++;
        else temp_y++;

        for (int i=0; i<playerNum; i++) {
            playerViews[i] = new PlayerView(i+1, temp_x, temp_y, colors[i]);
            StackPane.setAlignment(playerViews[i], Pos.BASELINE_CENTER);
            playerViews[i].relocate(temp_x * TILE_SIZE + 12, temp_y * TILE_SIZE + temp_margin);
            playerViews[i].getPlayer().setPos(temp_x, temp_y);
            temp_margin += 14;
            root.getChildren().add(playerViews[i]);
        }

    }

    private void initPlayerInfo(){
        int width = 720;
        StackPane infoPane = new StackPane();
        infoPane.setPrefSize(width, 120);
        infoPane.relocate(2 * TILE_SIZE, 10);
        infoPane.setAlignment(Pos.CENTER_LEFT);


        Rectangle rectangle = new Rectangle(width, 120);
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(2);

        Label label = new Label("Boards");
        label.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 16));
        StackPane.setAlignment(label, Pos.TOP_CENTER);
        StackPane.setMargin(label, new Insets(8, 0, 0, 0));
        infoPane.getChildren().addAll(rectangle, label);

        StackPane[] playerStackPane = new StackPane[playerNum];
        Rectangle[] playerRect = new Rectangle[playerNum];
        playerCards = new Label[playerNum][5];
        int xMargin = 0;
        for(int i=0; i<playerNum; i++){
            playerStackPane[i] = new StackPane();
            playerStackPane[i].setPrefSize(width/ playerNum, 100);
            StackPane.setMargin(playerStackPane[i], new Insets(30, 0, 0, xMargin));
            playerStackPane[i].setAlignment(Pos.TOP_CENTER);

            playerRect[i] = new Rectangle();
            playerRect[i].setWidth(width/ playerNum);
            playerRect[i].setHeight(100);
            playerRect[i].setStroke(Color.BLACK);
            playerRect[i].setStrokeWidth(2);
            playerRect[i].setFill(Color.WHITE);
            StackPane.setAlignment(playerRect[i], Pos.CENTER_LEFT);
            playerStackPane[i].getChildren().add(playerRect[i]);

            playerCards[i][0] = new Label("Player " + (i + 1));
            playerCards[i][0].setFont(Font.font("Arial",  FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 14));
            playerCards[i][0].setTextFill(colors[i]);
            StackPane.setAlignment(playerCards[i][0], Pos.TOP_LEFT);
            StackPane.setMargin(playerCards[i][0], new Insets(10, 0, 0, 20));

            playerCards[i][1] = new Label("Bridge Card : " + playerViews[i].getPlayer().getBridgeCardNum() + " cards");
            playerCards[i][1].setFont(Font.font("Arial",  FontWeight.BOLD, FontPosture.REGULAR, 12));
            StackPane.setAlignment(playerCards[i][1], Pos.TOP_LEFT);
            StackPane.setMargin(playerCards[i][1], new Insets(30, 0, 0, 20));

            playerCards[i][2] = new Label("Saw Card : " + playerViews[i].getPlayer().getSawCardNum() + " cards");
            playerCards[i][2].setFont(Font.font("Arial",  FontWeight.BOLD, FontPosture.REGULAR, 12));
            StackPane.setAlignment(playerCards[i][2], Pos.TOP_LEFT);
            StackPane.setMargin(playerCards[i][2], new Insets(45, 0, 0, 20));

            playerCards[i][3] = new Label("Hammer Card : " + playerViews[i].getPlayer().getHammerCardNum() + " cards");
            playerCards[i][3].setFont(Font.font("Arial",  FontWeight.BOLD, FontPosture.REGULAR, 12));
            StackPane.setAlignment(playerCards[i][3], Pos.TOP_LEFT);
            StackPane.setMargin(playerCards[i][3], new Insets(60, 0, 0, 20));

            playerCards[i][4] = new Label("Philips Card : " + playerViews[i].getPlayer().getPhilipsCardNum() + " cards");
            playerCards[i][4].setFont(Font.font("Arial",  FontWeight.BOLD, FontPosture.REGULAR, 12));
            StackPane.setAlignment(playerCards[i][4], Pos.TOP_LEFT);
            StackPane.setMargin(playerCards[i][4], new Insets(75, 0, 0, 20));

            playerStackPane[i].getChildren().addAll(playerCards[i][0], playerCards[i][1], playerCards[i][2], playerCards[i][3], playerCards[i][4]);
            infoPane.getChildren().add(playerStackPane[i]);

            xMargin += width / playerNum;
        }

        root.getChildren().add(infoPane);
    }

    private void initTurnInfo(){
        StackPane turnPane = new StackPane();
        turnPane.setPrefSize(350, 125);
        turnPane.relocate(820, 15);

        Rectangle rectangle = new Rectangle(350, 125);
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(2);

        turnLabel = new Label("Player " + turn + " Turn");
        turnLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));
        turnLabel.setTextFill(colors[turn-1]);
        turnPane.setAlignment(Pos.TOP_CENTER);
        StackPane.setMargin(turnLabel, new Insets(10, 0, 0, 0));

        Button diceButton = new Button("Roll a dice");
        diceButton.setPrefSize(120, 30);
        diceButton.setFont(Font.font("Arial",14));
        StackPane.setMargin(diceButton, new Insets(45, 0, 0 , 0));

        Button restButton = new Button("Rest");
        restButton.setPrefSize(120, 30);
        restButton.setFont(Font.font("Arial",14));

        if (playerViews[0].getPlayer().getBridgeCardNum() == 0) restButton.setDisable(true);
        StackPane.setMargin(restButton, new Insets(80, 0, 0, 0));

        diceButton.setOnAction(actionEvent -> {
            myItemClick.onRollDiceClick(diceButton, restButton);
        });

        restButton.setOnAction(actionEvent -> {
            myItemClick.onRestClick();
            changeTurn(restButton);
        });

        turnPane.getChildren().addAll(rectangle, turnLabel, diceButton, restButton);

        root.getChildren().add(turnPane);
    }

    public void changeTurn(Button restButton){
        turn = (playerNum == turn) ? 1 : turn + 1;
        turnLabel.setText("Player " + turn + " Turn");
        turnLabel.setTextFill(colors[turn-1]);

        if (playerViews[turn - 1].getPlayer().getBridgeCardNum() == 0) restButton.setDisable(true);
        else restButton.setDisable(false);
    }

    public void updatePlayerCardInfo(String type, int i){
        int temp = playerViews[turn-1].getPlayer().getTypeCardNum(type);
        playerCards[turn-1][i].setText(type + " Card : " + temp + " cards");
    }

    public boolean checkMoveInfo(String moveStr){
        int curX = getCurrentPlayer().getXPos();
        int curY = getCurrentPlayer().getYPos();
        for (int i=0; i<moveStr.length(); i++){
            Character temp = moveStr.charAt(i);
            switch (temp){
                case 'U':
                    curY--;
                    break;
                case 'D':
                    curY++;
                    break;
                case 'R':
                    curX++;
                    break;
                case 'L':
                    curX--;
                    break;
                default:
                    return false;
            }
            if (tiles[curX][curY] == null)
                return false;

            if (endCount >= 1){
                if (tiles[curX][curY].isBackMove(temp.toString()))
                    return false;
            }

        }
        return true;
    }

    public void move(String moveStr, Button exitButton, Label rollLabel){
        Thread thread = new Thread(){
            private int curX = getCurrentPlayer().getXPos();
            private int curY = getCurrentPlayer().getYPos();
            public void run(){
                try{
                    for (int i=0; i<moveStr.length(); i++){
                        Character temp = moveStr.charAt(i);
                        switch (temp){
                            case 'U':
                                curY--;
                                break;
                            case 'D':
                                curY++;
                                break;
                            case 'R':
                                curX++;
                                break;
                            case 'L':
                                curX--;
                                break;
                        }
                        Thread.sleep(1000);
                        switch (tiles[curX][curY].getType()){
                            case "BRIDGE":
                                playerViews[turn-1].setIsOnBridge(true, temp);
                                break;
                            case "SAW":
                                getCurrentPlayer().gainSawCard();
                                tiles[curX][curY].removeCard();
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        updatePlayerCardInfo("Saw", 2);
                                    }
                                });
                                break;
                            case "HAMMER":
                                getCurrentPlayer().gainHammerCard();
                                tiles[curX][curY].removeCard();
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        updatePlayerCardInfo("Hammer", 3);
                                    }
                                });
                                break;
                            case "PHILIPS":
                                getCurrentPlayer().gainPhilipsCard();
                                tiles[curX][curY].removeCard();
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        updatePlayerCardInfo("Philips", 4);
                                    }
                                });
                                break;
                            case "END":
                                endCount++;
                                playerViews[turn-1].relocate(curX * TILE_SIZE + 8, curY * TILE_SIZE + 8);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        exitButton.setDisable(false);
                                        if (endCount == 1){
                                            rollLabel.setText("Player "+ turn + " finish!!\nCannot move backwards since now.");
                                        }
                                        else rollLabel.setText("Player "+ turn + " finish!!\n");
                                    }
                                });
                                this.interrupt();
                                return;
                            default:
                                if (playerViews[turn-1].getIsOnBridge()){
                                    if (playerViews[turn-1].checkBridgeCrossed(temp)){
                                        getCurrentPlayer().gainBridgeCard();
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                updatePlayerCardInfo("Bridge", 1);
                                            }
                                        });
                                    }
                                    playerViews[turn-1].setIsOnBridge(false, null);
                                }
                        }
                        playerViews[turn-1].relocate(curX * TILE_SIZE + 8, curY * TILE_SIZE + 8);
                        getCurrentPlayer().setPos(curX, curY);
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            exitButton.setDisable(false);
                            rollLabel.setText("Turn Finished!! \nClick Exit Button.");
                        }
                    });
                    this.interrupt();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
