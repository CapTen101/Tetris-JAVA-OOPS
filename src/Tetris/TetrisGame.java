/*
 * This File: TetrisGame.java
 * Author: Rajan Budhathoki
 * Date: 23/05/2021
 * Purpose: This class defines the logic behind this Tetris game using data members and methods
 */
package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TetrisGame extends JPanel {

    // Define constants -> dimensions and piece fall interval for the game board below
    private final int WIDTH_OF_BOARD;
    private final int HEIGHT_OF_BOARD;

    private Timer timer;

    // Checks if piece has finished falling to spawn the next piece
    private boolean isFallingFinished = false;

    // Checks if the game is paused
    private boolean isPaused = false;

    // Checks the number of lines we have removed so far
    private int numRemovedLines = 0;

    // Below two variables give position of current falling piece
    private int currentX = 0;
    private int currentY = 0;
    private int score = 0;

    private JLabel statusBar;
    private Shape currentShape;
    private Shape.Tetrominoe[] board;

    public TetrisGame(TetrisWindow parent, int WIDTH_OF_BOARD, int HEIGHT_OF_BOARD) {
        initBoard(parent);
        this.WIDTH_OF_BOARD = WIDTH_OF_BOARD;
        this.HEIGHT_OF_BOARD = HEIGHT_OF_BOARD;
    }

    private void initBoard(TetrisWindow parent) {
        setFocusable(true);
        statusBar = parent.getStatusBar();

        // Init the Key Listener
        addKeyListener(new TetrisKeyAdapter());
    }

    Shape.Tetrominoe shapeAt(int x, int y) {
        return board[(y * WIDTH_OF_BOARD) + x];
    }

    // This method starts the game as well as spawns new pieces
    void startGame() {
        currentShape = new Shape();
        board = new Shape.Tetrominoe[WIDTH_OF_BOARD * HEIGHT_OF_BOARD];

        clearBoard();
        spawnBrick();

        timer = new Timer(300, new RepeatGameCycle());
        timer.start();
    }

    // This method pauses the game on pressing 'P' key
    void pauseGame() {
        isPaused = !isPaused;

        if (isPaused) {
            statusBar.setText("Paused...");
        } else {
            score = numRemovedLines * 100;
            statusBar.setText(String.valueOf(score));
        }

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - HEIGHT_OF_BOARD * squareHeight();

        for (int i = 0; i < HEIGHT_OF_BOARD; i++) {
            for (int j = 0; j < WIDTH_OF_BOARD; j++) {
                Shape.Tetrominoe shape = shapeAt(j, HEIGHT_OF_BOARD - i - 1);

                if (shape != Shape.Tetrominoe.NoShape) {
                    transferColor(
                            g,
                            j * squareWidth(),
                            boardTop + i * squareHeight(),
                            shape);
                }
            }
        }

        if (getCurrentShape().getShape() != Shape.Tetrominoe.NoShape) {
            for (int i = 0; i < 4; i++) {
                int x = getCurrentX() + getCurrentShape().getX(i);
                int y = getCurrentY() - getCurrentShape().getY(i);

                transferColor(
                        g,
                        x * squareWidth(),
                        boardTop + (HEIGHT_OF_BOARD - y - 1) * squareHeight(),
                        getCurrentShape().getShape());
            }
        }
    }

    void dropToBottom() {
        int newY = currentY;

        while (newY > 0) {
            if (!movePiece(currentShape, currentX, newY - 1)) {
                break;
            }

            newY--;
        }

        pieceDropped();
    }

    int squareWidth() {
        return (int) getSize().getWidth() / WIDTH_OF_BOARD;
    }

    int squareHeight() {
        return (int) getSize().getHeight() / HEIGHT_OF_BOARD;
    }

    // helper Method
    void moveOneLineDown() {
        if (!movePiece(currentShape, currentX, currentY - 1)) {
            pieceDropped();
        }
    }

    private void clearBoard() {
        for (int i = 0; i < HEIGHT_OF_BOARD * WIDTH_OF_BOARD; i++) {
            board[i] = Shape.Tetrominoe.NoShape;
        }
    }

    public Shape getCurrentShape() {
        return currentShape;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    private void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = currentX + currentShape.getX(i);
            int y = currentY - currentShape.getY(i);
            board[(y * WIDTH_OF_BOARD) + x] = currentShape.getShape();
        }

        removeFullLines();

        if (!isFallingFinished) {
            spawnBrick();
        }
    }

    private void spawnBrick() {
        currentShape.setRandomShape();
        currentX = (WIDTH_OF_BOARD / 2) + 1;
        currentY = HEIGHT_OF_BOARD - 1 + currentShape.minY();

        if (!movePiece(currentShape, currentX, currentY)) {
            currentShape.setShape(Shape.Tetrominoe.NoShape);
            timer.stop();

            String msg = String.format("Game over! Score: %d", numRemovedLines * 100);
            statusBar.setText(msg);
        }
    }

    public boolean movePiece(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.getX(i);
            int y = newY - newPiece.getY(i);

            if (x < 0 || x >= WIDTH_OF_BOARD || y < 0 || y >= HEIGHT_OF_BOARD) {
                return false;
            }

            if (shapeAt(x, y) != Shape.Tetrominoe.NoShape) {
                return false;
            }
        }

        currentShape = newPiece;
        currentX = newX;
        currentY = newY;

        repaint();

        return true;
    }

    private void removeFullLines() {
        int numFullLines = 0;

        for (int i = HEIGHT_OF_BOARD - 1; i >= 0; i--) {
            boolean isLineFull = true;

            for (int j = 0; j < WIDTH_OF_BOARD; j++) {
                if (shapeAt(j, i) == Shape.Tetrominoe.NoShape) {
                    isLineFull = false;
                    break;
                }
            }

            if (isLineFull) {
                numFullLines++;

                for (int k = i; k < HEIGHT_OF_BOARD - 1; k++) {
                    for (int j = 0; j < WIDTH_OF_BOARD; j++) {
                        board[(k * WIDTH_OF_BOARD) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }

        if (numFullLines > 0) {
            numRemovedLines += numFullLines;

            statusBar.setText(String.valueOf(numRemovedLines * 100));
            isFallingFinished = true;
            currentShape.setShape(Shape.Tetrominoe.NoShape);
        }
    }

    void transferColor(Graphics g, int x, int y, Shape.Tetrominoe shape) {

        // Defining the colors for our tetromino shapes
        Color[] colors = {
                new Color(0, 0, 0),
                new Color(239, 35, 35),
                new Color(17, 76, 217),
                new Color(112, 211, 238),
                new Color(23, 212, 40),
                new Color(243, 230, 87),
                new Color(33, 13, 132),
                new Color(197, 67, 234)
        };

        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(
                x + 1,
                y + 1,
                squareWidth() - 2,
                squareHeight() - 2
        );

        g.setColor(color.brighter());
        g.drawLine(
                x,
                y + squareHeight() - 1,
                x,
                y);
        g.drawLine(
                x,
                y,
                x + squareWidth() - 1,
                y);

        g.setColor(color.darker());
        g.drawLine(
                x + 1,
                y + squareHeight() - 1,
                x + squareWidth() - 1,
                y + squareHeight() - 1);
        g.drawLine(
                x + squareWidth() - 1,
                y + squareHeight() - 1,
                x + squareWidth() - 1,
                y + 1);
    }

    private class RepeatGameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateBoard();
            repaint();
        }
    }

    private void updateBoard() {
        if (isPaused) {
            return;
        }

        if (isFallingFinished) {
            isFallingFinished = false;
            spawnBrick();
        } else {
            moveOneLineDown();
        }
    }

    void newGame() {
        currentShape.setShape(Shape.Tetrominoe.NoShape);
        timer.stop();

        startGame();
    }

    private void translateKey(KeyEvent event) {
        if (currentShape.getShape() == Shape.Tetrominoe.NoShape) {
            return;
        }

        int eventKeyCode = event.getKeyCode();

        // Java 14 enhanced-switch lambda expressions
        // Through enhanced switch, we can bind key events to respective methods
        // Suggested by my IDE
        switch (eventKeyCode) {
            case KeyEvent.VK_SPACE -> pauseGame();
            case KeyEvent.VK_LEFT -> movePiece(currentShape, currentX - 1, currentY);
            case KeyEvent.VK_RIGHT -> movePiece(currentShape, currentX + 1, currentY);
            case KeyEvent.VK_DOWN -> movePiece(currentShape.rotateRight(), currentX, currentY);
            case KeyEvent.VK_UP -> movePiece(currentShape.rotateLeft(), currentX, currentY);
            case KeyEvent.VK_D -> dropToBottom();
            case KeyEvent.VK_F -> moveOneLineDown();
            case KeyEvent.VK_N -> newGame();
        }
    }

    class TetrisKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent event) {
            translateKey(event);
        }
    }
}
