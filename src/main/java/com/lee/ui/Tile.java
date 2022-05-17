package com.lee.ui;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Tile extends StackPane {
    private Rectangle rectangle = new Rectangle();

    public Tile(String type, int x, int y){
        if (type.equals("Start")){
            rectangle.setWidth(BridgeMapView.TILE_SIZE * 2);
            rectangle.setHeight(BridgeMapView.TILE_SIZE * 2);
            Text text = new Text("START");
            Font font = Font.font("Verdana", FontWeight.EXTRA_BOLD, 16);
            text.setFont(font);
            relocate(x * BridgeMapView.TILE_SIZE, y * BridgeMapView.TILE_SIZE);
            rectangle.setFill(Color.GOLD);
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(3);
            getChildren().addAll(rectangle, text);
        }
        else {
            rectangle.setWidth(BridgeMapView.TILE_SIZE);
            rectangle.setHeight(BridgeMapView.TILE_SIZE);
            relocate(x * BridgeMapView.TILE_SIZE, y * BridgeMapView.TILE_SIZE);
            rectangle.setFill(Color.valueOf("feb"));
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(3);
            getChildren().addAll(rectangle);
        }
    }
}
