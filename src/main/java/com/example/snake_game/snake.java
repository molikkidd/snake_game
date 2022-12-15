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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
                        tick(gc);
                        return;
                    }

                    if(now - lastTick > 1000000000/ speed) {
                        lastTick = now;
                        tick(gc);
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

            snake.add(new Corner(width/2,height/2));
            snake.add(new Corner(width/2,height/2));
            snake.add(new Corner(width/2,height/2));

//            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("SNAKE GAME");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void tick(GraphicsContext gc) {
        if(gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("",50));
            gc.fillText("GAME OVER",100,250);
            return;
        }
        for (int i = snake.size() - 1; i>=1; i--) {
            snake.get(i).x = snake.get(i-1).x;
            snake.get(i).y = snake.get(i-1).y;
        }

        switch (direction) {
            case up:
                snake.get(0).y--;
                if(snake.get(0).y < 0) {
                    gameOver = true;
                }
            case down:
                snake.get(0).y++;
                if(snake.get(0).y > height) {
                    gameOver = true;
                }
            case left:
                snake.get(0).x--;
                if(snake.get(0).x < 0) {
                    gameOver = true;
                }
            case right:
                snake.get(0).x++;
                if(snake.get(0).x > width) {
                    gameOver = true;
                }
                break;
        }
        if(foodX == snake.get(0).x && foodY == snake.get(0).y) {
            snake.add(new Corner(-1,-1));
            newFood();
        }

        for(int i = 1; i < snake.size(); i++) {
            if(snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y){
                gameOver = true;
            }
        }
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,width*cornersize, height*cornersize);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("",30));
        gc.fillText("Score",+(speed-6), 10,30);

        Color cc = Color.WHITE;

        switch (foodcolor) {
            case 0: cc = Color.PURPLE;
                break;
            case 1: cc = Color.BLUE;
                break;
            case 2: cc = Color.GREEN;
                break;
            case 3: cc = Color.ORANGE;
                break;
            case 4: cc = Color.RED;
                break;
        }

        gc.setFill(cc);
        gc.fillOval(foodX*cornersize, foodY*cornersize, cornersize, cornersize);

//        snake

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
