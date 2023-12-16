package com.Snake.app;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Snake{

    private List<Point> snakeBody = new ArrayList();
    private Point snakeHead;
    GraphicsContext gc;

    private int currentDirection;
    private int oldDirection;
    private String color;

    public Snake(GraphicsContext gc, int x, int y, String color){
        this.gc = gc;
        this.color = color;
        this.currentDirection = Control.RIGHT;

        for (int i = 0; i < 3; i++){
                snakeBody.add(new Point(x, y));
            }
        this.snakeHead = this.snakeBody.get(0);
    }
    public void drawSnake(GraphicsContext gc) {
        gc.setFill(Color.web(color));
        
        gc.fillRoundRect(snakeHead.getX() * App.getSQUARE_SIZE(), snakeHead.getY() * App.getSQUARE_SIZE(), App.getSQUARE_SIZE() - 1, App.getSQUARE_SIZE() - 1, 35, 35);

        for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).getX() * App.getSQUARE_SIZE(), snakeBody.get(i).getY() * App.getSQUARE_SIZE(), App.getSQUARE_SIZE() - 1,
                    App.getSQUARE_SIZE() - 1, 20, 20);
        }
        
    }
    public void move(){

        for (int i = snakeBody.size() -1; i>=1;i--){
            snakeBody.get(i).x = snakeBody.get(i-1).x;
            snakeBody.get(i).y = snakeBody.get(i-1).y;  
        }

        this.oldDirection = this.currentDirection;
        switch (currentDirection){
            case Control.RIGHT:
                moveRight();
                break;
            case Control.LEFT:
                moveLeft();
                break;
            case Control.UP:
                moveUP();
                break;
            case Control.DOWN:
                moveDown();
                break;
        }
    }
    public boolean gameOver(boolean gameOver, Snake snake2){

        //Hit wall
        if(snakeHead.x < 0
         || snakeHead.y < 0
         || snakeHead.x * App.getSQUARE_SIZE() >= App.getWIDTH()
         || snakeHead.y * App.getSQUARE_SIZE() >= App.getHEIGHT()){

            gameOver = true;
            System.out.print("HIT Wall");
        }
        //Hit body
        for (int i = 1 ; i < snakeBody.size();i++){
            if(snakeHead.x ==  snakeBody.get(i).getX() && snakeHead.getY() == snakeBody.get(i).getY()){
                gameOver = true;
                System.out.print("HIT BODY");
                break;
            }
        }
        //Hit other snake
        if(snake2 != null){
            for (int i = 1; i < snake2.getBody().size(); i++){
                if(snakeHead.x ==  snake2.getBody().get(i).getX() && snakeHead.y == snake2.getBody().get(i).getY()){
                gameOver = true;
                System.out.print("HIT Other snake");
                break;
            }
            }
        }
        return gameOver;
    }
    public void moveRight(){
        snakeHead.x++;
    }
    public void moveLeft(){
        snakeHead.x--;
    }

    public void moveUP(){
        snakeHead.y--;
    }
    public void moveDown(){
        snakeHead.y++;
    }

    public int getCurrentDirection(){
        return currentDirection;
    }
    public int getOldDirection(){
        return oldDirection;
    }
    public void setCurrentDirection(int dir){
        currentDirection = dir;
    }
    public void setOldDirection(int dir){
        oldDirection = dir;
    }
    public List<Point> getBody(){
        return snakeBody;
    }
    public Point getHead(){
        return snakeHead;
    }
    public void clear(){
        snakeBody.clear();
    }

}