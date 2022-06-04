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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;


public class BridgeMapView {
    public static final int TILE_SIZE = 36;
    public static final Color[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.PURPLE};
    public static int WIDTH = 25;
    public static int HEIGHT = 18;
    public static int START_X = 3;
    public static int START_Y = 5;
    private int x, y;
    private Pane root;
    private ScrollPane scrollPane;
    private StackPane infoPane, turnPane;
    private Group tileGroup = new Group();
    private Tile[][] tiles;
    private BridgeMap map;

    private int playerNum = 0;
    private int turn = 1;
    private int endCount = 0;
    private int[] endOrders;
    private PlayerView[] playerViews;
    private Label[][] playerCards;
    private Label turnLabel;
    private onItemClick myItemClick;
    public interface onItemClick {
        void onRollDiceClick(Button diceButton, Button restButton);

        void onRestClick();

        void onExitClick();
    }

    public BridgeMapView() {
        initMap();
    }

    public void setMyClickListener(onItemClick myItemClick) {
        this.myItemClick = myItemClick;
    }

    public Parent getContentPane() {
        return root;
    }

    public Parent getScrollPane() {
        return scrollPane;
    }

    public Player getCurrentPlayer() {
        return playerViews[turn - 1].getPlayer();
    }

    public void initMap() {
        map = BridgeMap.getInstance();
        playerNum = BridgeMainController.getPlayerNum();
        playerViews = new PlayerView[playerNum];

        initEndOrders();
        String[] mapData = map.getMapData();
        initMapSize();
        if (mapData == null) {
            throw new IllegalArgumentException("map 데이터가 null 일 수 없습니다.");
        }

        String prevMove = null;
        for (int i = 0; i < mapData.length; i++) {
            String[] temp = mapData[i].split(" ");
            Tile tile;
            if (i == 0 && temp[0].equals("S")) {
                tile = new Tile("START", x, y, null, temp[1]);
                switch (temp[1]) {
                    case "R":
                        tiles[x + 1][y] = tile;
                        x += 2;
                        break;
                    case "D":
                        tiles[x][y + 1] = tile;
                        y += 2;
                        break;
                }
                initPlayerPos(temp[1]);
            } else if (temp[0].equals("E")) {
                if (prevMove.equals("U") || prevMove.equals("R"))
                    tile = new Tile("END", x, y - 1, null, null);
                else tile = new Tile("END", x, y, null, null);
                tiles[x][y] = tile;

            } else if (temp[0].equals("S")) {
                tile = new Tile("SAW", x, y, temp[1], temp[2]);
                tiles[x][y] = tile;
                setNextXY(temp[2]);
            } else if (temp[0].equals("P")) {
                tile = new Tile("PHILIPS", x, y, temp[1], temp[2]);
                tiles[x][y] = tile;
                setNextXY(temp[2]);
            } else if (temp[0].equals("H")) {
                tile = new Tile("HAMMER", x, y, temp[1], temp[2]);
                tiles[x][y] = tile;
                setNextXY(temp[2]);
            } else if (temp[0].equals("B")) {
                tile = new Tile("INTERSECT", x, y, temp[1], temp[2]);
                tiles[x][y] = tile;
                Tile bridge = new Tile("BRIDGE", x + 1, y, "L", "R");
                tiles[x + 1][y] = bridge;
                tileGroup.getChildren().add(bridge);
                setNextXY(temp[2]);
            } else {
                tile = new Tile("CELL", x, y, temp[1], temp[2]);
                tiles[x][y] = tile;
                setNextXY(temp[2]);

            }
            tileGroup.getChildren().add(tile);
            if (1 <= i && i < mapData.length - 1) prevMove = temp[2];

        }

        initPlayerInfo();
        initTurnInfo();

    }

    private void initEndOrders() {
        endOrders = new int[playerNum];
        for (int i = 0; i < playerNum; i++) {
            endOrders[i] = -1;
        }
    }

    private void initMapSize() {
        int width = map.getMapWidth();
        int height = map.getMapHeight();
        int widthGap = map.getStartWidthGap();
        int heightGap = map.getStartHeightGap();

        START_X += widthGap;
        START_Y += heightGap;
        WIDTH = START_X + width;
        HEIGHT = START_Y + height;

        x = START_X;
        y = START_Y;
        tiles = new Tile[WIDTH + 1][HEIGHT + 1];

        scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        root = new Pane();
        root.setPrefSize(WIDTH * TILE_SIZE + 150, HEIGHT * TILE_SIZE + 150);
        root.getChildren().add(tileGroup);
        scrollPane.setContent(root);
    }



    private void initPlayerPos(String s) {
        int temp_x = START_X;
        int temp_y = START_Y;

        int temp_XMargin = 0;
        int temp_YMargin = 0;

        if (s.equals("R")) {
            temp_XMargin = 12;
            temp_YMargin = 4;
            temp_x++;
        } else {
            temp_YMargin = 12;
            temp_XMargin = 4;
            temp_y++;
        }

        for (int i = 0; i < playerNum; i++) {
            playerViews[i] = new PlayerView(i + 1, temp_x, temp_y, colors[i]);
            StackPane.setAlignment(playerViews[i], Pos.BASELINE_CENTER);
            playerViews[i].relocate(temp_x * TILE_SIZE + temp_XMargin, temp_y * TILE_SIZE + temp_YMargin);
            playerViews[i].getPlayer().setPos(temp_x, temp_y);
            if (s.equals("R")) temp_YMargin += 12;
            else temp_XMargin += 12;
            root.getChildren().add(playerViews[i]);
        }
    }

    private void initPlayerInfo() {
        int width = 720;
        infoPane = new StackPane();
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
        for (int i = 0; i < playerNum; i++) {
            playerStackPane[i] = new StackPane();
            playerStackPane[i].setPrefSize(width / playerNum, 100);
            StackPane.setMargin(playerStackPane[i], new Insets(30, 0, 0, xMargin));
            playerStackPane[i].setAlignment(Pos.TOP_CENTER);

            playerRect[i] = new Rectangle();
            playerRect[i].setWidth(width / playerNum);
            playerRect[i].setHeight(100);
            playerRect[i].setStroke(Color.BLACK);
            playerRect[i].setStrokeWidth(2);
            playerRect[i].setFill(Color.WHITE);
            StackPane.setAlignment(playerRect[i], Pos.CENTER_LEFT);
            playerStackPane[i].getChildren().add(playerRect[i]);

            playerCards[i][0] = new Label("Player " + (i + 1));
            playerCards[i][0].setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 14));
            playerCards[i][0].setTextFill(colors[i]);
            StackPane.setAlignment(playerCards[i][0], Pos.TOP_LEFT);
            StackPane.setMargin(playerCards[i][0], new Insets(10, 0, 0, 20));

            playerCards[i][1] = new Label("Bridge Card : " + playerViews[i].getPlayer().getBridgeCardNum() + " cards");
            playerCards[i][1].setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 12));
            StackPane.setAlignment(playerCards[i][1], Pos.TOP_LEFT);
            StackPane.setMargin(playerCards[i][1], new Insets(30, 0, 0, 20));

            playerCards[i][2] = new Label("Saw Card : " + playerViews[i].getPlayer().getSawCardNum() + " cards");
            playerCards[i][2].setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 12));
            StackPane.setAlignment(playerCards[i][2], Pos.TOP_LEFT);
            StackPane.setMargin(playerCards[i][2], new Insets(45, 0, 0, 20));

            playerCards[i][3] = new Label("Hammer Card : " + playerViews[i].getPlayer().getHammerCardNum() + " cards");
            playerCards[i][3].setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 12));
            StackPane.setAlignment(playerCards[i][3], Pos.TOP_LEFT);
            StackPane.setMargin(playerCards[i][3], new Insets(60, 0, 0, 20));

            playerCards[i][4] = new Label("Philips Card : " + playerViews[i].getPlayer().getPhilipsCardNum() + " cards");
            playerCards[i][4].setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 12));
            StackPane.setAlignment(playerCards[i][4], Pos.TOP_LEFT);
            StackPane.setMargin(playerCards[i][4], new Insets(75, 0, 0, 20));

            playerStackPane[i].getChildren().addAll(playerCards[i][0], playerCards[i][1], playerCards[i][2], playerCards[i][3], playerCards[i][4]);
            infoPane.getChildren().add(playerStackPane[i]);

            xMargin += width / playerNum;
        }

        root.getChildren().add(infoPane);
    }

    private void initTurnInfo() {
        turnPane = new StackPane();
        turnPane.setPrefSize(350, 125);
        turnPane.relocate(820, 15);

        Rectangle rectangle = new Rectangle(350, 125);
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(2);

        turnLabel = new Label("Player " + turn + " Turn");
        turnLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));
        turnLabel.setTextFill(colors[turn - 1]);
        turnPane.setAlignment(Pos.TOP_CENTER);
        StackPane.setMargin(turnLabel, new Insets(10, 0, 0, 0));

        Button diceButton = new Button("Roll a dice");
        diceButton.setPrefSize(120, 30);
        diceButton.setFont(Font.font("Arial", 14));
        StackPane.setMargin(diceButton, new Insets(45, 0, 0, 0));

        Button restButton = new Button("Rest");
        restButton.setPrefSize(120, 30);
        restButton.setFont(Font.font("Arial", 14));

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

    private void setNextXY(String t) {
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
            case "L":
                x--;
                break;
        }
    }

    public void changeTurn(Button restButton) {
        turn = (playerNum == turn) ? 1 : turn + 1;
        while (endOrders[turn - 1] != -1) {
            turn = (playerNum == turn) ? 1 : turn + 1;
        }
        turnLabel.setText("Player " + turn + " Turn");
        turnLabel.setTextFill(colors[turn - 1]);

        restButton.setDisable(playerViews[turn - 1].getPlayer().getBridgeCardNum() == 0);
    }

    public void updatePlayerCardInfo(String type, int i) {
        int temp = playerViews[turn - 1].getPlayer().getTypeCardNum(type);
        playerCards[turn - 1][i].setText(type + " Card : " + temp + " cards");
    }

    public boolean checkMoveInfo(String moveStr) {
        int curX = getCurrentPlayer().getXPos();
        int curY = getCurrentPlayer().getYPos();
        for (int i = 0; i < moveStr.length(); i++) {
            Character temp = moveStr.charAt(i);
            switch (temp) {
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
            if (curX > WIDTH || curY > HEIGHT)
                return false;

            if (tiles[curX][curY] == null)
                return false;

            if (tiles[curX][curY].getType() == "END")
                return true;

            if (endCount >= 1) {
                if (tiles[curX][curY].isBackMove(temp.toString())) {
                    return false;
                }
            }

        }
        return true;
    }

    public void move(String moveStr, Button exitButton, Label rollLabel) {
        Thread thread = new Thread() {
            private int curX = getCurrentPlayer().getXPos();
            private int curY = getCurrentPlayer().getYPos();

            public void run() {
                try {
                    for (int i = 0; i < moveStr.length(); i++) {
                        Character temp = moveStr.charAt(i);
                        switch (temp) {
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
                        Thread.sleep(500);
                        switch (tiles[curX][curY].getType()) {
                            case "BRIDGE":
                                playerViews[turn - 1].setIsOnBridge(true, temp);
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
                                endOrders[turn - 1] = endCount;
                                getCurrentPlayer().gainRankScore(endCount);

                                playerViews[turn - 1].relocate(curX * TILE_SIZE + 8, curY * TILE_SIZE + 8);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        exitButton.setDisable(false);
                                        if (endCount == 1) {
                                            rollLabel.setText("Player " + turn + " finish!!\nCannot move backwards\nfrom now on.");
                                        } else rollLabel.setText("Player " + turn + " finish!!\n");
                                    }
                                });

                                if (endCount == playerNum - 1) {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            endGame();
                                        }
                                    });
                                    this.interrupt();
                                }
                                return;

                            default:
                                if (playerViews[turn - 1].getIsOnBridge()) {
                                    if (playerViews[turn - 1].checkBridgeCrossed(temp)) {
                                        getCurrentPlayer().gainBridgeCard();
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                updatePlayerCardInfo("Bridge", 1);
                                            }
                                        });
                                    }
                                    playerViews[turn - 1].setIsOnBridge(false, null);
                                }
                        }
                        playerViews[turn - 1].relocate(curX * TILE_SIZE + 8, curY * TILE_SIZE + 8);
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void endGame() {
        turnPane.getChildren().remove(2);
        turnPane.getChildren().remove(2);

        turnLabel.setText("End Game");
        turnLabel.setTextFill(Color.BLACK);
        turnPane.setAlignment(Pos.TOP_CENTER);
        StackPane.setMargin(turnLabel, new Insets(10, 0, 0, 0));

        Button exitButton = new Button("Exit");
        exitButton.setPrefSize(120, 30);
        exitButton.setFont(Font.font("Arial", 14));
        StackPane.setMargin(exitButton, new Insets(45, 0, 0, 0));
        turnPane.getChildren().add(exitButton);
        exitButton.setOnAction(actionEvent -> {
            myItemClick.onExitClick();
        });

        StackPane resPane = new StackPane();
        resPane.setPrefSize(350, 230);
        resPane.relocate(820, 200);
        resPane.setAlignment(Pos.TOP_CENTER);
        root.getChildren().add(resPane);

        Rectangle rectangle = new Rectangle(350, 230);
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(2);
        resPane.getChildren().add(rectangle);

        Label scoreLabel = new Label("Score");
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 18));
        StackPane.setAlignment(scoreLabel, Pos.TOP_CENTER);
        StackPane.setMargin(scoreLabel, new Insets(10, 0, 0, 0));
        resPane.getChildren().add(scoreLabel);

        Label[] playerScores = new Label[playerNum];
        int yMargin = 0;
        for (int i = 0; i < playerNum; i++) {
            if (playerNum == 3 && endOrders[i] == -1) playerViews[i].getPlayer().gainRankScore(3);
            else if (playerNum == 2 && endOrders[i] == -1) playerViews[i].getPlayer().gainRankScore(2);
            playerScores[i] = new Label("Player " + (i + 1) + " : " + playerViews[i].getPlayer().getScore() + " points");
            playerScores[i].setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 17));
            playerScores[i].setTextFill(colors[i]);
            StackPane.setAlignment(playerScores[i], Pos.CENTER_LEFT);
            StackPane.setMargin(playerScores[i], new Insets(yMargin, 0, 20, 30));
            resPane.getChildren().add(playerScores[i]);
            yMargin += 60;
        }

        int winnerId = getFirstRankPlayerId();
        Label resLabel = new Label("Winner is Player " + winnerId + " !!");
        resLabel.setTextFill(Color.FIREBRICK);
        resLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 24));
        StackPane.setAlignment(resLabel, Pos.TOP_CENTER);
        StackPane.setMargin(resLabel, new Insets(45, 0, 0, 0));

        resPane.getChildren().add(resLabel);
    }

    private int getFirstRankPlayerId() {
        int max = -1, idx = 0;
        int temp;
        for (int i = 0; i < playerNum; i++) {
            temp = playerViews[i].getPlayer().getScore();
            if (max < temp) {
                max = temp;
                idx = i;
            } else if (max == temp) {
                if (endOrders[idx] > i)
                    idx = i;
            }
        }
        return (idx + 1);
    }


}

