package com.lee.model;

public class Player{
    // 플레이어 정보들
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

    // rest
    public void rest(){
        bridgeCardNum--;
    }

    // getter들
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

    public int getScore() {return score;}

    // 카드를 얻은 경우
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

    // 종료 지점에 들어온 순위에 대한 점수를 얻는 함수
    public void gainRankScore(int rank){
        switch (rank){
            case 1:
                score += 7;
                break;
            case 2:
                score += 3;
                break;
            case 3:
                score += 1;
                break;
        }
    }
}
