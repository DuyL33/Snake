package com.Snake.app;



import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;

public class Control{

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    public void keyHandle(Scene scene, Snake snake, Snake snake2){

        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
                @Override
                public void handle(KeyEvent event){
                    KeyCode code = event.getCode();

                    if(code == KeyCode.RIGHT || code == KeyCode.D){
                        if(snake.getOldDirection() != LEFT){
                            System.out.print("d");
                            snake.setCurrentDirection(RIGHT);
                        }
                    }
                    else if(code == KeyCode.LEFT || code == KeyCode.A){

                        if(snake.getOldDirection() != RIGHT){
                            System.out.print("a");
                            snake.setCurrentDirection(LEFT);
                        }
                    }
                    else if(code == KeyCode.UP || code == KeyCode.W){
                        if(snake.getOldDirection() != DOWN){
                            System.out.print("w");
                            snake.setCurrentDirection(UP);
                        }
                    }
                    else if(code == KeyCode.DOWN || code == KeyCode.S){
                        if(snake.getOldDirection() != UP){
                            System.out.print("s");
                            snake.setCurrentDirection(DOWN);
                        }
                    }
                    else if(code == KeyCode.L){
                        if(snake2.getOldDirection() != LEFT){
                            //System.out.print("d");
                            snake2.setCurrentDirection(RIGHT);
                        }
                    }
                    else if(code == KeyCode.J){

                        if(snake2.getOldDirection() != RIGHT){
                            //System.out.print("a");
                            snake2.setCurrentDirection(LEFT);
                        }
                    }
                    else if(code == KeyCode.I){
                        if(snake2.getOldDirection() != DOWN){
                            //System.out.print("w");
                            snake2.setCurrentDirection(UP);
                        }
                    }
                    else if(code == KeyCode.K){
                        if(snake.getOldDirection() != UP){
                            System.out.print("s");
                            snake2.setCurrentDirection(DOWN);
                        }
                    }
                }
            });
    }
}