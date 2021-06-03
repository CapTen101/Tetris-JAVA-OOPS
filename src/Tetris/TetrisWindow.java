/*
 * This File: TetrisWindow.java
 * Author: Tushar Rohilla
 * Date: 23/05/2021
 * Purpose: This class defines the basic external window (JFrame) for our tetris program
 */
package Tetris;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class TetrisWindow extends JFrame {

    // statusBar will display the score
    private final JLabel statusBar;

    public TetrisWindow() {
        System.out.println("INSTRUCTIONS BELOW:");
        System.out.println("Press N -> to restart the game");
        System.out.println("Press Space Bar -> pauses the game");
        System.out.println("Press F -> speeds up the falling piece speed");
        System.out.println("Press D -> to immediately drop the piece and spawn new one\n\n");

        statusBar = new JLabel(" 0");
        add(statusBar, BorderLayout.SOUTH);

        int WIN_WIDTH = 11;
        int WIN_HEIGHT = 25;

        TetrisGame tetrisGame = new TetrisGame(this, WIN_WIDTH, WIN_HEIGHT);
        add(tetrisGame);
        tetrisGame.startGame();

        setTitle("Tetris");
        setSize(200, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    JLabel getStatusBar() {
        return statusBar;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            TetrisWindow game = new TetrisWindow();
            game.setVisible(true);
        });
    }
}
