/*
 * This File: TetrisDisplay.java
 * Author: Tushar Rohilla
 * Date: 23/05/2021
 * Purpose: This class defines the listeners and graphical logic required for the tetris game
 */
package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TetrisDisplay extends JPanel {
    private TetrisGame tetrisGame;
    private int start_x;
    private int start_y;

    public TetrisDisplay(TetrisGame tetrisGame, int start_x, int start_y) {
        this.tetrisGame = tetrisGame;
        this.start_x = start_x;
        this.start_y = start_y;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - start_y * tetrisGame.squareHeight();

        for (int i = 0; i < start_y; i++) {
            for (int j = 0; j < start_x; j++) {
                Shape.Tetrominoe shape = tetrisGame.shapeAt(j, start_y - i - 1);

                if (shape != Shape.Tetrominoe.NoShape) {
                    tetrisGame.transferColor(
                            g,
                            j * tetrisGame.squareWidth(),
                            boardTop + i * tetrisGame.squareHeight(),
                            shape);
                }
            }
        }

        if (tetrisGame.getCurrentShape().getShape() != Shape.Tetrominoe.NoShape) {
            for (int i = 0; i < 4; i++) {
                int x = tetrisGame.getCurrentX() + tetrisGame.getCurrentShape().getX(i);
                int y = tetrisGame.getCurrentY() - tetrisGame.getCurrentShape().getY(i);

                tetrisGame.transferColor(
                        g,
                        x * tetrisGame.squareWidth(),
                        boardTop + (start_y - y - 1) * tetrisGame.squareHeight(),
                        tetrisGame.getCurrentShape().getShape());
            }
        }
    }

    private void translateKey(KeyEvent event) {
        if (tetrisGame.getCurrentShape().getShape() == Shape.Tetrominoe.NoShape) {
            return;
        }

        int eventKeyCode = event.getKeyCode();

        switch (eventKeyCode) {
            case KeyEvent.VK_SPACE -> tetrisGame.pauseGame();
            case KeyEvent.VK_LEFT -> tetrisGame.movePiece(tetrisGame.getCurrentShape(), tetrisGame.getCurrentX() - 1, tetrisGame.getCurrentY());
            case KeyEvent.VK_RIGHT -> tetrisGame.movePiece(tetrisGame.getCurrentShape(), tetrisGame.getCurrentX() + 1, tetrisGame.getCurrentY());
            case KeyEvent.VK_DOWN -> tetrisGame.movePiece(tetrisGame.getCurrentShape().rotateRight(), tetrisGame.getCurrentX(), tetrisGame.getCurrentY());
            case KeyEvent.VK_UP -> tetrisGame.movePiece(tetrisGame.getCurrentShape().rotateLeft(), tetrisGame.getCurrentX(), tetrisGame.getCurrentY());
            case KeyEvent.VK_D -> tetrisGame.dropToBottom();
            case KeyEvent.VK_F -> tetrisGame.moveOneLineDown();
            case KeyEvent.VK_N -> tetrisGame.newGame();
        }
    }

    class TetrisKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent event) {
            translateKey(event);
        }
    }
}
