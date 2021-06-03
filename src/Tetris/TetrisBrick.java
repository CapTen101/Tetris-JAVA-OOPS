/*
 * This File: TetrisBrick.java
 * Author: Rajan Budhathoki
 * Date: 23/05/2021
 * Purpose: This source file defines the abstract class which has the abstract functions for every brick class
 */

package Tetris;

public abstract class TetrisBrick {

    int[][] positions = new int[4][2];
    int orientation;
    int numSegments;
    int colorNumber;

    public TetrisBrick() {
    }

    public int getOrientation() {
        return orientation;
    }

    public int getNumSegments() {
        return numSegments;
    }

    public int getColorNumber() {
        return colorNumber;
    }

    public void setPositions(int[][] positions) {
        this.positions = positions;
    }

    public int[][] getPositions() {
        return positions;
    }

    abstract void initPosition();

    abstract void rotate();

    abstract void unRotate();

    abstract void moveLeft();

    abstract void moveRight();

    abstract void moveUp();

    abstract void moveDown();
}
