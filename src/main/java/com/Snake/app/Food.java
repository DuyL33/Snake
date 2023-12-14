package com.Snake.app;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import java.awt.Point;
public class Food{

    private Image foodImage;
    private int foodX;
    private int foodY;
    private static final int WIDTH = 800;
    private static final int ROWS = 20;
    private static final int COLS = ROWS;
    private static final int SQUARE_SIZE = WIDTH/ ROWS;

    private static final String[] FOOD_IMAGE = new String[]{
    "/img/ic_apple.png",
    "/img/ic_berry.png",
    "/img/ic_cherry.png",
    "/img/ic_coconut_.png",
    "/img/ic_orange.png",
    "/img/ic_peach.png",
    "/img/ic_pomegranate.png",
    "/img/ic_tomato.png",
    "/img/ic_watermelon.png"
    };

    public void generateFood(Snake snake){
            start:
            while(true){
                foodX = (int)(Math.random()* ROWS);
                foodY = (int)(Math.random()* COLS);
                for(Point snakeBlock: snake.getBody()){
                    if(snakeBlock.getX() == foodX && snakeBlock.getY() == foodY){
                        continue start;
                    }
                }
                foodImage = new Image(FOOD_IMAGE[(int)(Math.random() * FOOD_IMAGE.length)]);
                break;

            }
    }
    public void drawFood(GraphicsContext gc){
        gc.drawImage(foodImage, foodX * SQUARE_SIZE, foodY*SQUARE_SIZE, SQUARE_SIZE,SQUARE_SIZE);
    }

    public int eatFood(Snake snake, int score){
        if (snake.getHead().getX() == foodX && snake.getHead().getY() == foodY){
            snake.getBody().add(new Point(-1,-1));
            generateFood(snake);
            score +=1;
        }
        return score;
    }


}