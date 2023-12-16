package com.Snake.app;


public class Player{
    private String pname;
    private int score;

    public Player(String pname, int score){
        this.pname = pname;
        this.score = score;
    }
    public String toString(){
        return pname + " : " + score + "\n";
    }
}