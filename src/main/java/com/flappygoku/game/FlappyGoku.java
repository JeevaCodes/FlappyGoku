package com.flappygoku.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class FlappyGoku extends JPanel implements ActionListener, KeyListener {

    int boardWidth = 360;
    int boardHeight = 640;

    //images
    Image backgroundImg;
    Image gokuImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //Goku class
    int gokuX = boardWidth/8;
    int gokuY = boardWidth/2;
    int gokuWidth = 80;
    int gokuHeight = 58;

    class Goku {

        int x = gokuX;
        int y = gokuY;
        int width = gokuWidth;
        int height = gokuHeight;
        Image img;

         Goku(Image img){
            this.img = img;
        }

    }

    //Pipe Class

    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe{

        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean isPassed = false;

        Pipe(Image img){
            this.img = img;
        }

    }

    //Game logic
    Goku goku;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipeTimer;
    boolean gameOver = false;
    double score = 0;

    FlappyGoku(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
       // setFocusable(true);
        addKeyListener(this);

            //loadimages
            backgroundImg = new ImageIcon(getClass().getResource("/bg.png")).getImage();
            gokuImg = new ImageIcon(getClass().getResource("/goku.png")).getImage();
            topPipeImg = new ImageIcon(getClass().getResource("/toppipe.png")).getImage();
            bottomPipeImg = new ImageIcon(getClass().getResource("/bottompipe.png")).getImage();

        //goku
        goku = new Goku(gokuImg);
        pipes = new ArrayList<Pipe>();

        //Place pipes timer
        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipeTimer.start();

        //game timer
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

    }

    void placePipes(){
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics){
        //Background
        graphics.drawImage(backgroundImg, 0, 0,this.boardWidth, this.boardHeight,null);
        //Goku
        graphics.drawImage(gokuImg, goku.x, goku.y, goku.width, goku.height, null);

        //pipes
        for( int i=0 ; i<pipes.size() ; i++ ){
            Pipe pipe = pipes.get(i);
            graphics.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //score
        graphics.setColor(Color.white);

        graphics.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver){
            graphics.drawString("Game Over :: "+String.valueOf(score),10,35);
        }else{
            graphics.drawString(String.valueOf(score),10,35);
        }

    }

    public void move(){

        velocityY += gravity;
        goku.y += velocityY;
        goku.y = Math.max(goku.y, 0 );

        //pipes
        for(int i=0; i<pipes.size() ; i++) {

            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.isPassed && goku.x > pipe.x + pipe.width) {
                score += 0.5;
                pipe.isPassed = true;
            }

            if (collision(goku, pipe)) {
                gameOver = true;
            }
        }

            if (goku.y > boardHeight) {
                gameOver = true;
            }

    }

    boolean collision(Goku a, Pipe b){
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y  + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;

            if(gameOver){
                goku.y = gokuY;
                velocityY = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placePipeTimer.start();

            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }


}
