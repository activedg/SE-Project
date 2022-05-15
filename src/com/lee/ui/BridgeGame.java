package com.lee.ui;

import com.lee.model.Dice;
import com.lee.model.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BridgeGame extends JFrame{
    Player[] players;

    BridgeGame(){
        setTitle("Bridge Game");
        setLayout(null);
        JButton btn1 = new JButton("플레이");
        // btn1.setPreferredSize(new Dimension(300, 100));
        btn1.setBounds(350, 450, 300, 100);
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // To be implemented
                btn1.setVisible(false);
                test();
                setBounds(0, 0, 1000, 1000);
            }
        });
        add(btn1);
        setBounds(0, 0 , 800, 800);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args){
        new BridgeGame();
    }

    private void initPlayers(){

    }

    private void playGame(){
        JPanel jp = new JPanel();
    }
    private void test(){
        JButton btn2 = new JButton("테스트");
        btn2.setBounds(500, 450, 300, 100);
        add(btn2);
    }

}
