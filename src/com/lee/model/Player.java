package com.lee.model;
import java.util.*;

public class Player{
    final int PIECE_SIZE = 20;
    private int cardNum = 2;
    private int xPos = 0;
    private int yPos = 0;

    public void moveToward(int xMov, int yMov){
        xPos += xMov;
        yPos += yMov;
    }

    public void moveBackward(int xMov, int yMov){
        xPos -= xMov;
        yPos -= yMov;
    }

    public void rest(){
        cardNum--;
    }
}
