package com.lee.model;
import java.util.*;

public class Dice {
    public static Dice dice = null;
    private int value = 0;

    public static Dice getInstance(){
        if (dice == null){
            dice = new Dice();
        }
        return dice;
    }

    private void rollDice() {
        value = (int) (Math.random() * 6  + 1);
    }

    // Get (Dice Result - Current Card Count)
    public int getMov(int cardCount){
        rollDice();
        int res = this.value - cardCount;

        if (res < 0) return 0;
        else return res;
    }
}
