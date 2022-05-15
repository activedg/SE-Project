package com.lee.ui;

import com.lee.model.Board;

import javax.swing.*;

public class BoardView extends JPanel {
    JPanel jp = new JPanel();
    public void test(){
        Board b = Board.getInstance();
        b.defaultMap();
        String[] temp;
        for (int i=0; i<b.map.length; i++){
            temp = b.map[i].split(" ");
            for (int j=0; j<temp.length; j++){

            }
        }
    }
}
