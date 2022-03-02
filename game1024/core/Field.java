package game1024.core;

import java.util.Random;

public class Field {

    private Tile[][] tiles;
    private final int rowCount;
    private final int columnCount;
    private FieldState state;
    private int score;

    public Field(int rowCount, int columnCount) throws IllegalArgumentException {
        if (rowCount != columnCount) {
            throw new IllegalArgumentException("Rows and columns of the field cannot be of different sizes");
        }
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        state = FieldState.PLAYING;
        score = 0;
        generateField();
    }

    public Tile getTile(int rowCount, int columnCount) { return tiles[rowCount][columnCount]; }
    public int getRowCount() { return rowCount; }
    public int getColumnCount() { return columnCount; }
    public int getScore() { return score; }
    public FieldState getState() { return state; }

    private void setState(FieldState state) {
        this.state = state;
    } // ??? i dont need ???

    private boolean allTilesDown(int boundFieldIndex) {
        boolean tilesChangedPosition = false;
        for (int i = 0; i < rowCount; i++) {
            for (int k = 0; k < columnCount-1; k++) { //3 times to be sure that all tiles will be go down
                for (int j = boundFieldIndex; j > 0; j--) {
                    if (tiles[j][i].getState() == TileState.EMPTY && tiles[j - 1][i].getState() == TileState.OCCUPIED) {
                        //last element in column = last-1 element in column
                        tiles[j][i].setValue(tiles[j - 1][i].getValue());
                        tiles[j][i].setState(TileState.OCCUPIED);
                        //last-1 element in column = 0
                        tiles[j - 1][i].setValue(0);
                        tiles[j - 1][i].setState(TileState.EMPTY);
                        tilesChangedPosition = true;
                    }
                }
            }
        }
        return tilesChangedPosition;
    }

    private void mergeTiles(int boundFieldIndex) {
        for (int i = 0; i < rowCount; i++) {
            for (int j = boundFieldIndex; j > 0; j--) {
                if (tiles[j][i].getState() == TileState.OCCUPIED && tiles[j - 1][i].getState() == TileState.OCCUPIED
                        && tiles[j][i].getValue() == tiles[j-1][i].getValue()){
                    //last element in column = last + last-1 element in column
                    tiles[j][i].setValue((tiles[j - 1][i].getValue()) + (tiles[j][i].getValue()));
                    tiles[j][i].setState(TileState.OCCUPIED);
                    raiseScore(tiles[j-1][i], tiles[j][i]);
                    //last-1 element in column = 0
                    tiles[j - 1][i].setValue(0);
                    tiles[j - 1][i].setState(TileState.EMPTY);
                }
            }
        }
    }

    private void raiseScore(Tile tile1, Tile tile2) {
        score += tile1.getValue() + tile2.getValue();
    }

    //move all tiles in the direction, merge them, and creates one new tile after
    public void moveTiles(MoveDirection direction) {
        int boundFieldIndex = columnCount-1;
        boolean tilesChangedPosition = false;

        if (direction == MoveDirection.DOWN) {
            if (allTilesDown(boundFieldIndex)) {
                initializeNewTile();
                tilesChangedPosition = true;
            }

            mergeTiles(boundFieldIndex);

            if (allTilesDown(boundFieldIndex) && !tilesChangedPosition) {
                initializeNewTile();
            }
        }
        else if (direction == MoveDirection.UP) {
            turnRightField();
            draw();
            turnRightField();
            draw();
            if (allTilesDown(boundFieldIndex)) {
                initializeNewTile();
                tilesChangedPosition = true;
            }
            mergeTiles(boundFieldIndex);
            if (allTilesDown(boundFieldIndex) && !tilesChangedPosition) {
                initializeNewTile();
            }
            draw();
            turnRightField();
            draw();
            turnRightField();
        }
    }

    private void draw(){
        System.out.println("score: " + getScore());
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                System.out.print("  " + tiles[i][j].getValue() + " ");
            }
            System.out.println("\n");
        }
    }

    public void turnRightField() {
        int[][] temp = new int[rowCount][columnCount];
        int rowToTake = rowCount-1;
        int columnToTake = 0;

        for (int rowToWrite = 0; rowToWrite < rowCount; rowToWrite++) {
            rowToTake = rowCount-1;
            for (int columnToWrite = 0; columnToWrite < columnCount; columnToWrite++) {
                temp[rowToWrite][columnToWrite] = tiles[rowToTake][columnToTake].getValue();
                rowToTake--;
            }
            columnToTake++;
        }

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                tiles[i][j].setValue(temp[i][j]);
            }
        }
    }

    public void updateGameState(){
        int emptyTilesCount = 0;

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (tiles[i][j].getValue() == 1024) { setState(FieldState.WON); }
                if (tiles[i][j].getValue() == 0) { emptyTilesCount++; }
            }
        }

        if (getState() != FieldState.WON) {
            if (emptyTilesCount == 0) {
                setState(FieldState.LOST);
            }
            else {
                setState(FieldState.PLAYING);
            }
        }
    }

    private void initializeNewTile() {
        Random rand = new Random();
        int randomRow, randomColumn;
        boolean isAlreadyInitialized = false;

        while (!isAlreadyInitialized){
            randomRow = rand.nextInt(rowCount);
            randomColumn = rand.nextInt(columnCount);
            if (tiles[randomRow][randomColumn].getState() == TileState.EMPTY) {
                tiles[randomRow][randomColumn].setValue(rand.nextInt(2)+1); //initialize tile by 1 or 2
                isAlreadyInitialized = true;
            }
        }
    }

    private void generateField() {
        //create and initialize field with zeros
        tiles = new Tile[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                tiles[i][j] = new Tile(0);
            }
        }
        //2 not empty tiles for start of the game

        initializeNewTile();
        initializeNewTile();
    }
}
