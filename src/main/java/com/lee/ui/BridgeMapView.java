package com.lee.ui;

import com.lee.model.BridgeMap;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class BridgeMapView {
    public static final int TILE_SIZE = 35;
    public static final int WIDTH = 25;
    public static final int HEIGHT = 25;

    private Pane root;
    private Group tileGroup = new Group();
    private BridgeMap map;

    public BridgeMapView(){
        map = BridgeMap.getInstance();
        root = new Pane();

        root.setPrefSize(WIDTH * TILE_SIZE + 100, HEIGHT * TILE_SIZE);
        root.getChildren().add(tileGroup);
        for (int j=1; j<HEIGHT / 2; j++){

            for (int i=1; i<WIDTH; i++){
                Tile tile;
                if (j == 1 && i == 1){
                    tile = new Tile("Start", i , j);
                }
                else if (j <= 2 && i<= 2) continue;
                else {
                    tile = new Tile("Cell", i, j);
                }
                tileGroup.getChildren().add(tile);
            }
        }
    }

    public Parent getContentPane(){
        return root;
    }

    private void setMap(){
        // 나중에 지워야 함
        map.defaultMap();
        String mapData[] = map.getMapData();
        if (mapData == null){
            throw new IllegalArgumentException("map 데이터가 null 일 수 없습니다.");
        }

        for (int i=0; i< mapData.length; i++){

        }
    }
    
    private void updatePlayerMove(){

    }
}
