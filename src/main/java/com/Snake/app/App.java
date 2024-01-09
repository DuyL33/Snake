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

import javafx.scene.control.TextInputDialog;
import java.util.Optional;

import java.sql.Connection;
import javafx.scene.control.Label;

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
    private final double targetFrameRate = 7.0;
    private final double targetFrameTime = 1.0 / targetFrameRate;

    private Snake snake;
    private Snake snake2;
    private Food food = new Food();
    private Button sb = new Button("Two Player");
    private Button sb2 = new Button("Single Player");
    private boolean singlePlayer = true;
    private AnimationTimer animationTimer;

    private String name;
    private DataBase db = new DataBase();
    private Label topplayer;
    private Label snakeGameText = new Label("Snake");

    @Override
    public void start(Stage primaryStage){
        try {
            primaryStage.setTitle("Snake");
            StackPane root = new StackPane();
            Canvas canvas = new Canvas(WIDTH,HEIGHT);

            Scene scene = new Scene(root);
            VBox vbox = new VBox(10);
            Control c = new Control();
            String cssPath = getClass().getResource("/styles.css").toExternalForm();
            topplayer = new Label(db.getTop5Players());
            vbox.getChildren().addAll(snakeGameText,topplayer, sb2, sb);

            gc = canvas.getGraphicsContext2D();

            root.getChildren().addAll(canvas, vbox);

            //load css into fx
            //String cssPath = getClass().getResource("/styles.css").toExternalForm();
            root.getStylesheets().add(cssPath);

            sb.getStyleClass().add("start-button");
            sb2.getStyleClass().add("start-button");
            vbox.getStyleClass().add("vbox");
            topplayer.getStyleClass().add("topPlayers");
            snakeGameText.getStyleClass().add("title");

            drawBackground(gc);
            primaryStage.setScene(scene);
            primaryStage.show();


            snake = new Snake(gc, 5, 2, "FFF200");
            snake2 = new Snake(gc, 5, 17, "FFC0CB");

            food.generateFood(snake);
            animationTimer = new AnimationTimer() {
                public void handle(long now){
                    double elapsedSeconds = (now - lastUpdateTime) / 1_000_000_000.0;
                    if (elapsedSeconds >= targetFrameTime) {
                        if(singlePlayer){
                            run2(gc, c, scene, vbox);
                        }
                        else{
                            run(gc, c, scene, vbox);
                        }
                        lastUpdateTime = now;
                    }
                }
            };

            c.keyHandle(scene,snake, snake2);
            startButton(primaryStage, root, scene, animationTimer, vbox);
            singleButton(primaryStage, root, scene, animationTimer, vbox);

        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause != null) {
                cause.printStackTrace();
            } else {
                e.printStackTrace();
            }
        }
        
    }
    private void run(GraphicsContext gc, Control c, Scene scene, VBox vbox){
        if(gameOver == true || gameOver2 == true){
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7",70));
            if(gameOver == true && gameOver2 == false){
                gc.fillText("Player2 Win",WIDTH/3.5, 120);
            }
            else if(gameOver == false && gameOver2 == true){
                gc.fillText("Player1 Win", WIDTH/3.5, 120);
            }
            else{
                gc.fillText("Draw", WIDTH/3.5, 120);
            }
            resetGame(scene, c);
            vbox.toFront();
            animationTimer.stop();
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
    private void run2(GraphicsContext gc, Control c, Scene scene, VBox vbox){
        if(gameOver == true){
            db.connect(name, score);
            topplayer.setText(db.getTop5Players());

            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7",70));
            gc.fillText("Game Over", WIDTH/3.5, 120);
            

            resetGame(scene, c);
            vbox.toFront();
            animationTimer.stop();
            return;
        }
        drawBackground(gc);
        drawScore();
        snake.drawSnake(gc);
        snake.move();
        food.drawFood(gc);
        gameOver = snake.gameOver(gameOver, null);
        score = food.eatFood(snake,score);
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
        gc.fillText("Score: " + score, 10,35);

    }
    private void drawScore2(){
        gc.setFill(Color.PINK);
        gc.setFont(new Font("Digital-7",35));
        gc.fillText("Score: " + score2, 650,35);

    }
    private void startButton(Stage primaryStage, StackPane root, Scene scene, AnimationTimer animationTimer, VBox vbox){
        sb.setOnAction(e -> {
                //root.getChildren().remove(vbox);
                singlePlayer = false;
                vbox.toBack();
                primaryStage.setScene(scene);
                animationTimer.start();
        });

    }
    private void singleButton(Stage primaryStage, StackPane root, Scene scene, AnimationTimer animationTimer, VBox vbox){
        sb2.setOnAction(e -> {
                //root.getChildren().remove(vbox);

                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Single Player");
                dialog.setHeaderText("Enter your name:");
                dialog.setContentText("Name:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(playerName -> {
                // Set the player's name (you can use it as needed)
                System.out.println("Player Name: " + playerName);
                name = playerName;

                // Proceed with the game
                singlePlayer = true;
                vbox.toBack();
                primaryStage.setScene(scene);
                animationTimer.start();
                });
        });

    }
    
    private void resetGame(Scene scene, Control c) {
        // Reset variables
        gameOver = false;
        gameOver2 = false;
        score = 0;
        score2 = 0;

        // Reset snakes
        snake = new Snake(gc, 5, 2, "FFF200");
        snake2 = new Snake(gc, 5, 17, "FFC0CB");
        c.keyHandle(scene,snake, snake2);
        // Generate new food
        food.generateFood(snake);

    }
    public static int getWIDTH(){
        return WIDTH;
    }
    public static int getHEIGHT(){
        return HEIGHT;
    }
    public static int getROWS(){
        return ROWS;
    }
    public static int getCOLS(){
        return COLS;
    }
    public static int getSQUARE_SIZE(){
        return SQUARE_SIZE;
    }





    public static void main(String[] args){
        launch(args);
    }
}
