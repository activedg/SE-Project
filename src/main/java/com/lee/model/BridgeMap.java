package com.lee.model;

public class BridgeMap {
    private static BridgeMap m = null;
    private String[] mapData;

    public static BridgeMap getInstance(){
        if (m == null)
            m = new BridgeMap();
        return m;
    }

    public void defaultMap(){
        mapData = new String[] {
                "S R", "C L R", "C L D", "B U D", "S U D", "C U D", "C U D", "C U R", "C L R", "H L U", "B D U", "C D U", "C D U",
                "C D U", "b D U", "P D U", "C D R", "C L R", "H L D", "C U D", "C U D", "C U D", "B U D", "C U D", "b U D","C U D",
                "S U D", "P U D", "C U D", "C U D", "C U R", "C L R", "C L U", "H D U", "C D U", "B D U", "C D U", "C D U", "C D U",
                "C D U", "b D U", "C D R", "C L R", "H L D", "C U D", "C U D", "C U D", "P U D", "B U D", "b U D", "C U D", "H U D",
                "C U R", "C L R", "S L U", "P D U", "C D U", "C D U", "b D U", "C D U", "C D U", "E"
        };
    }
    public String[] getMapData(){
        return mapData;
    }


    public void setMapData(){

    }
}