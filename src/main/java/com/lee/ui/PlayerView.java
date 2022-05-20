package com.lee.ui;

import com.lee.model.Player;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PlayerView extends StackPane {
    private Player player;
    private Circle circle;
    private Text text;

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

    public Player getPlayer() {
        return player;
    }
    public Boolean getIsOnBridge() { return isOnBridge; }

    public void setIsOnBridge(Boolean isOnBridge, Character moveType){
        this.isOnBridge = isOnBridge;
        this.moveType = moveType;
    }

    public boolean checkBridgeCrossed(Character nowMove){
        if (isOnBridge && this.moveType == nowMove) return true;
        return false;
    }
}
