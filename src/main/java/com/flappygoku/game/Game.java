package com.flappygoku.game;

import javax.swing.*;

public class Game {
    public static void main(String[] args) {

        int boardWidth = 360;
        int boardHeight = 640;

        JFrame jFrame = new JFrame("Flappy Goku");
        jFrame.setSize(boardWidth, boardHeight);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyGoku goku = new FlappyGoku();
        jFrame.add(goku);
        jFrame.pack();
        goku.requestFocus();
        jFrame.setVisible(true);
    }
}
