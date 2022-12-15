package com.example.snake_game;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class snake extends Application {
    static int speed = 5;
    static int foodcolor = 0;
    static int width = 20;
    static int height = 20;
    static int foodY = 0;
    static int foodX = 0;
    static int cornersize = 25;
    static List<Corner> snake = new ArrayList<>();
    static Dir direction = Dir.left;
    static boolean gameOver = false;
    static Random rand = new Random();

    public enum Dir{
        left,right,up,down
    }
    public static class Corner {
        int x;
        int y;

        public Corner(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            newFood();

            VBox root = new VBox();
            Canvas c = new Canvas(width*cornersize,height*cornersize);
            GraphicsContext gc = c.getGraphicsContext2D();
            root.getChildren().add(c);
            new AnimationTimer(){
                long lastTick = 0;

                public void handle (long now) {
                    if(lastTick == 0) {
                        lastTick = now;
                        return;
                    }

                    if(now - lastTick > 1000000000/ speed) {
                        lastTick = now;
                    }
                }
            }.start();

            Scene scene = new Scene(root, width*cornersize, height*cornersize);

            scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
                if(keyEvent.getCode() == KeyCode.W) {
                    direction = Dir.up;
                }
                if(keyEvent.getCode() == KeyCode.A) {
                    direction = Dir.left;
                }
                if(keyEvent.getCode() == KeyCode.S) {
                    direction = Dir.down;
                }
                if(keyEvent.getCode() == KeyCode.D) {
                    direction = Dir.right;
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void newFood() {
        start: while(true) {
            foodX = rand.nextInt(width);
            foodY = rand.nextInt(height);

            for(Corner c:snake) {
                if(c.x == foodX && c.y == foodY) {
                    continue start;
                }
            }
            foodcolor = rand.nextInt(5);
            speed++;
            break;
        }
    }
}
