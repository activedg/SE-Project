package com.lee.ui;

import com.lee.model.Player;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PlayerView extends StackPane {
    // 플레이어
    private Player player;
    private Circle circle;
    private Text text;

    // 다리를 지나가는지 판단용
    private Boolean isOnBridge = false;
    private Character moveType;

    PlayerView(int num, int xPos, int yPos, Color color){
        this.player = new Player(xPos, yPos);
        circle = new Circle();
        circle.setRadius(12);
        circle.setFill(color);
        text = new Text(Integer.toString(num));
        text.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 18));
        text.setFill(Color.WHITE);
        getChildren().addAll(circle, text);
    }

    // getter
    public Player getPlayer() {
        return player;
    }
    public Boolean getIsOnBridge() { return isOnBridge; }

    public void setIsOnBridge(Boolean isOnBridge, Character moveType){
        this.isOnBridge = isOnBridge;
        this.moveType = moveType;
    }

    // 다리를 지나갔는지 체크하는 함수
    public boolean checkBridgeCrossed(Character nowMove){
        if (isOnBridge && this.moveType == nowMove) return true;
        return false;
    }
}
