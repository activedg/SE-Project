package com.lee.model;

public class Board {
    private static Board m = null;
    public String[] map;

    public static Board getInstance(){
        if (m == null)
            m = new Board();
        return m;
    }

    public void defaultMap(){
        map = new String[] {
                "S R", "C L R", "C L D", "B U D", "S U D", "C U D", "C U D", "C U R", "C L R", "H L U", "B D U", "C D U", "C D U",
                ""
        };
    }


    private void setMap(){

    }
}
