/*
 * This File: JayBrick.java
 * Author: Rajan Budhathoki
 * Date: 23/05/2021
 * Purpose: This class defines the JayBrick class
 */
package Tetris;

public class JayBrick extends TetrisBrick {
    public JayBrick() {
    }

    @Override
    void initPosition() {
        // Every piece:
        // maximum 4 units along x
        // maximum 2 units along y
        // hence [4][2]
        positions = new int[][]{
                {1, -1},
                {0, -1},
                {0, 0},
                {0, 1}
        };
    }

    @Override
    void rotate() {
        Shape currShape = new Shape();
        if (currShape.getShape() == Shape.Tetrominoe.Square) {
            return;
        }

        Shape newShape = new Shape();
        newShape.setShape(currShape.getShape());

        for (int i = 0; i < 4; i++) {
            newShape.getX(positions[i][0]);
            newShape.getY(positions[i][0]);
        }

        return;
    }

    @Override
    void unRotate() {
        Shape currShape = new Shape();
        Shape.Tetrominoe oldShape = currShape.getShape();

        currShape.setShape(oldShape);
    }

    @Override
    void moveLeft() {
        Shape currShape = new Shape();
        int x = -1;
        for (int i = 0; i < 4; i++) {
            x = currShape.getX(i);
        }

        x--;
        currShape.setX(currShape.getShape().ordinal(), x);
    }

    @Override
    void moveRight() {
        Shape currShape = new Shape();
        int x = -1;
        for (int i = 0; i < 4; i++) {
            x = currShape.getX(i);
            x++;
            currShape.setX(currShape.getShape().ordinal(), x);
        }
    }

    @Override
    void moveUp() {
        Shape currShape = new Shape();
        int y = -1;
        for (int i = 0; i < 4; i++) {
            y = currShape.getY(i);
            y++;
            currShape.setY(currShape.getShape().ordinal(), y);
        }
    }

    @Override
    void moveDown() {
        Shape currShape = new Shape();
        int y = -1;
        for (int i = 0; i < 4; i++) {
            y = currShape.getY(i);
            y--;
            currShape.setY(currShape.getShape().ordinal(), y);
        }
    }
}
