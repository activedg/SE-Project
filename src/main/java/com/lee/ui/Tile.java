package com.lee.ui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;

public class Tile extends StackPane {
    private String type;
    private Rectangle rectangle = new Rectangle();
    private String backMove;
    private String frontMove;

    public Tile(String type, int x, int y, String backMove, String frontMove){
        this.type = type;
        this.backMove = backMove;
        this.frontMove = frontMove;

        if (type.equals("START") || type.equals("END")){
            rectangle.setWidth(BridgeMapView.TILE_SIZE * 2);
            rectangle.setHeight(BridgeMapView.TILE_SIZE * 2);
            Text text = new Text(type);
            Font font = Font.font("Verdana", FontWeight.EXTRA_BOLD, 16);
            text.setFont(font);
            relocate(x * BridgeMapView.TILE_SIZE, y * BridgeMapView.TILE_SIZE);
            rectangle.setFill(Color.GOLD);
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(3);
            getChildren().addAll(rectangle, text);
        }
        else if (type.equals("BRIDGE")){
            relocate(x * BridgeMapView.TILE_SIZE, y * BridgeMapView.TILE_SIZE);
            File file = new File("src/main/resources/com/lee/image/bridge.png");
            ImageView image = new ImageView(new Image(file.toURI().toString()));
            image.setFitWidth(BridgeMapView.TILE_SIZE);
            image.setFitHeight(BridgeMapView.TILE_SIZE);
            getChildren().addAll(image);
        }
        else {
            rectangle.setWidth(BridgeMapView.TILE_SIZE);
            rectangle.setHeight(BridgeMapView.TILE_SIZE);
            relocate(x * BridgeMapView.TILE_SIZE, y * BridgeMapView.TILE_SIZE);
            rectangle.setFill(Color.valueOf("feb"));
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(3);
            if (type.equals("SAW")){
                File file = new File("src/main/resources/com/lee/image/saw.png");
                ImageView image = new ImageView(new Image(file.toURI().toString()));
                image.setFitWidth(30);
                image.setFitHeight(30);
                getChildren().addAll(rectangle, image);
            } else if (type.equals("PHILIPS")){
                File file = new File("src/main/resources/com/lee/image/philipsDriver.png");
                ImageView image = new ImageView(new Image(file.toURI().toString()));
                image.setFitWidth(30);
                image.setFitHeight(30);
                getChildren().addAll(rectangle, image);
            } else if (type.equals("HAMMER")){
                File file = new File("src/main/resources/com/lee/image/hammer.png");
                ImageView image = new ImageView(new Image(file.toURI().toString()));
                image.setFitWidth(30);
                image.setFitHeight(30);
                getChildren().addAll(rectangle, image);
            } else if (type.equals("INTERSECT")){
                File file = new File("src/main/resources/com/lee/image/intersect.png");
                ImageView image = new ImageView(new Image(file.toURI().toString()));
                image.setFitWidth(25);
                image.setFitHeight(25);
                getChildren().addAll(rectangle, image);
            }

            else getChildren().addAll(rectangle);
        }
    }

    public String getType() {return type;}

    public void removeCard(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getChildren().remove(1);
            }
        });
        type = "CELL";
    }

    public boolean isBackMove(String backMove){
        return this.backMove.equals(backMove);
    }
}
