package com.Snake.app;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.animation.AnimationTimer;


public class App extends Application{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final int ROWS = 20;
    private static final int COLS = ROWS;
    private static final int SQUARE_SIZE = WIDTH/ ROWS;

    private GraphicsContext gc;

    private boolean gameOver;
    private boolean gameOver2;
    private int score = 0;
    private int score2 = 0;


    private long lastUpdateTime = 0;
    private final double targetFrameRate = 10.0;
    private final double targetFrameTime = 1.0 / targetFrameRate;

    private Snake snake;
    private Snake snake2;
    private Food food = new Food();
    private Button sb = new Button("Two Player");
    private Button sb2 = new Button("Single Player");

    @Override
    public void start(Stage primaryStage){
        try {

            primaryStage.setTitle("Snake");
            StackPane root = new StackPane();
            Canvas canvas = new Canvas(WIDTH,HEIGHT);

            Scene scene = new Scene(root);
            VBox vbox = new VBox(8);
            AnimationTimer animationTimer;
            Control c = new Control();

            vbox.getChildren().addAll(sb2, sb);

            gc = canvas.getGraphicsContext2D();

            root.getChildren().addAll(canvas, vbox);

            //load css into fx
            String cssPath = getClass().getResource("/styles.css").toExternalForm();
            root.getStylesheets().add(cssPath);

            vbox.getStyleClass().add("start-button");


            primaryStage.setScene(scene);
            primaryStage.show();


            snake = new Snake(gc, SQUARE_SIZE, 5, 2, "FFF200");
            snake2 = new Snake(gc, SQUARE_SIZE, 5, 17, "FFC0CB");

            food.generateFood(snake);
            animationTimer = new AnimationTimer() {
                
                public void handle(long now){
                    double elapsedSeconds = (now - lastUpdateTime) / 1_000_000_000.0;
                    if (elapsedSeconds >= targetFrameTime) {
                        run(gc);

                        lastUpdateTime = now;
                    }
                }
            };
            c.keyHandle(scene,snake, snake2);
            startButton(primaryStage, root, scene, animationTimer, vbox);

        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause != null) {
                cause.printStackTrace();
            } else {
                e.printStackTrace();
            }
        }
        
    }
    private void run(GraphicsContext gc){
        if(gameOver == true || gameOver2 == true){
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7",70));
            gc.fillText("Game Over", WIDTH/3.5, HEIGHT/2);

            return;
        }
        
        drawBackground(gc);
        drawScore();
        drawScore2();
        snake.drawSnake(gc);
        snake.move();
        snake2.drawSnake(gc);
        snake2.move();

        food.drawFood(gc);
        
        gameOver = snake.gameOver(gameOver, snake2);
        gameOver2 = snake2.gameOver(gameOver2, snake);

        score = food.eatFood(snake,score);
        score2 = food.eatFood(snake2,score2);

    }
    private void drawBackground(GraphicsContext gc){
        for(int i = 0; i<ROWS;i++){
            for(int j = 0; j<ROWS;j++){
                if((i+j)%2 == 0){
                    gc.setFill(Color.web("016064"));
                }
                else{
                    gc.setFill(Color.web("022D36"));
                }
                gc.fillRect(i*SQUARE_SIZE, j*SQUARE_SIZE,SQUARE_SIZE,SQUARE_SIZE);
            }

        }
    }
    
    private void drawScore(){
        gc.setFill(Color.YELLOW);
        gc.setFont(new Font("Digital-7",35));
        gc.fillText("Score1: " + score, 10,35);

    }
    private void drawScore2(){
        gc.setFill(Color.PINK);
        gc.setFont(new Font("Digital-7",35));
        gc.fillText("Score2: " + score2, 650,35);

    }
    private void startButton(Stage primaryStage, StackPane root, Scene scene, AnimationTimer animationTimer, VBox vbox){
        sb.setOnAction(e -> {
                root.getChildren().remove(vbox);
                primaryStage.setScene(scene);
                animationTimer.start();

        });

    }

    public static void main(String[] args){
        launch(args);

    }
}
