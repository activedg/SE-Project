package com.lee.model;

public class Player{
    private int bridgeCardNum = 0;
    private int sawCardNum = 0;
    private int hammerCardNum = 0;
    private int philipsCardNum = 0;
    private int score = 0;
    private int xPos = 0;
    private int yPos = 0;


    public Player(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void setPos(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void rest(){
        bridgeCardNum--;
    }

    public int getBridgeCardNum() {return bridgeCardNum;}

    public int getSawCardNum() {return sawCardNum;}

    public int getHammerCardNum() {return hammerCardNum;}

    public int getPhilipsCardNum() {return philipsCardNum;}

    public int getTypeCardNum(String cardType){
        if (cardType.equals("Bridge"))
            return getBridgeCardNum();
        else if (cardType.equals("Saw"))
            return getSawCardNum();
        else if (cardType.equals("Hammer"))
            return getHammerCardNum();
        else return getPhilipsCardNum();
    }

    public int getXPos() {return xPos;}

    public int getYPos() {return yPos;}

    public void gainBridgeCard() {
        bridgeCardNum++;
    }

    public void gainSawCard() {
        sawCardNum++;
        score += 3;
    }

    public void gainHammerCard() {
        hammerCardNum++;
        score += 2;
    }

    public void gainPhilipsCard() {
        philipsCardNum++;
        score += 1;
    }
}
