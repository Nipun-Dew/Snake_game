/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Nipun Dewenarayane
 */
public class GamePanel extends JPanel implements ActionListener{
    
    private final int SCREEN_WIDTH = 600;
    private final int SCREEN_HEIGHT = 600;
    private final int UNIT_SIZE = 20;
    private final int DELAY = 70;
    private final int GAME_UNITS = (SCREEN_WIDTH/UNIT_SIZE*SCREEN_HEIGHT/UNIT_SIZE);
    private final int[] x = new int[GAME_UNITS];
    private final int[] y = new int[GAME_UNITS];
    
    private int bodyParts = 6;
    private int applesEaten = 0;
    private int appleX, appleY;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private final Random random;
    
    GamePanel(){
        
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.white);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    
    private void startGame(){
        
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        draw(g);
    }
    
    private void draw(Graphics g){
        if(running){
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
                
            for(int i=0; i<bodyParts; i++){
                if(i==0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    g.setColor(new Color(41, 171, 89));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            
            g.setColor(Color.black);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score :  "+applesEaten, (SCREEN_WIDTH-metrics.stringWidth("Score :  "+applesEaten))/2, 30);
        }
        else{
            GameOver(g);
        }
    }
    
    private void newApple(){
        
        appleX = random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }
    
    private void move(){
        for(int i=bodyParts-1; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        
        switch(direction){
            
            case 'R' -> {
                if(x[0]>=(SCREEN_WIDTH-UNIT_SIZE)){
                    x[0] = 0;
                    break;
                }
                x[0] = x[0] + UNIT_SIZE;
            }
                
            case 'L' -> {
                if(x[0]<=0){
                    x[0] = SCREEN_WIDTH - UNIT_SIZE;
                    break;
                }
                x[0] = x[0] - UNIT_SIZE;
            }
                
            case 'U' -> {
                if(y[0]<=0){
                    y[0] = SCREEN_HEIGHT - UNIT_SIZE;
                    break;
                }
                y[0] = y[0] - UNIT_SIZE;
            }
                
            case 'D' -> {
                if(y[0]>=SCREEN_HEIGHT - UNIT_SIZE){
                    y[0] = 0;
                    break;
                }
                y[0] = y[0] + UNIT_SIZE;
            }
        }
    }
    
    private void checkApple(){
        
        if(x[0]==appleX && y[0]==appleY){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    
    private void checkCollissions(){
        
        for(int i=1; i<bodyParts; i++){
            if(x[0]==x[i] && y[0]==y[i]){
                running = false;
            }
        }
        
        if(!running)
            timer.stop();
        
        
    }
    
    private void GameOver(Graphics g){
        g.setColor(Color.black);
        g.setFont(new Font("Ink Free", Font.BOLD, 55));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over!!!", (SCREEN_WIDTH-metrics.stringWidth("Game Over!!!"))/2, SCREEN_HEIGHT/2);
        
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Your Score is :  "+applesEaten, (SCREEN_WIDTH-metrics2.stringWidth("Your Score is :  "+applesEaten))/2, 40);
    }
    
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT -> {
                    if(direction != 'R')
                        direction = 'L';
                }
                    
                case KeyEvent.VK_RIGHT -> {
                    if(direction != 'L')
                        direction = 'R';
                }
                    
                case KeyEvent.VK_UP -> {
                    if(direction != 'D')
                        direction = 'U';
                }
                    
                case KeyEvent.VK_DOWN -> {
                    if(direction != 'U')
                        direction = 'D';
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollissions();
        }
        repaint();
    }
    
}
