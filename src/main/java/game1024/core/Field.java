package game1024.core;

import java.util.Random;

public class Field {

    private Tile[][] tiles;
    private final int rowCount;
    private final int columnCount;
    private FieldState state;
    private int score;

    public Field(int rowCount, int columnCount) throws IllegalArgumentException {
        if (rowCount != columnCount || rowCount < 4) {
            throw new IllegalArgumentException("Rows and columns of the field cannot be of the different sizes.\n" +
                                               "Also it cannot be 0x0, 1x1, 2x2 and 3x3\n");
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
    }

    private boolean allTilesDown(boolean fieldStateChanged) {
        for (int i = 0; i < rowCount; i++) {
            for (int k = 0; k < columnCount-1; k++) { //3 times to be sure that all tiles will be go down
                for (int j = columnCount-1; j > 0; j--) {
                    if (tiles[j][i].getValue() == 0 && tiles[j - 1][i].getValue() != 0) {
                        //"last element" in column will have initialized by "last-1 element" element in column
                        tiles[j][i].setValue(tiles[j - 1][i].getValue());
                        //"last-1 element" in column will have initialized by 0
                        tiles[j - 1][i].setValue(0);
                        fieldStateChanged = true;
                    }
                }
            }
        }
        return fieldStateChanged;
    }

    private boolean mergeTiles(boolean fieldStateChanged) {
        for (int i = 0; i < rowCount; i++) {
            for (int j = columnCount-1; j > 0; j--) {
                if (tiles[j][i].getValue() != 0 && tiles[j - 1][i].getValue() != 0
                                                && tiles[j][i].getValue() == tiles[j-1][i].getValue()){
                    //"last element" in column will be initialized by "last + last-1" element in column
                    tiles[j][i].setValue((tiles[j - 1][i].getValue()) + (tiles[j][i].getValue()));
                    //"last-1 element" in column will be initialized by  0
                    tiles[j - 1][i].setValue(0);
                    fieldStateChanged = true;
                    raiseScore(tiles[j-1][i].getValue(), tiles[j][i].getValue());
                }
            }
        }
        return fieldStateChanged;
    }

    private void raiseScore(int tile1, int tile2) {
        score += tile1 + tile2;
    }

    //rotates field down depending on direction.
    //then move all tiles down, merge, and move it down again.
    //rotate field back to original state.
    //if field state has changed - create new tile on the field. Update game state.
    public void moveTiles(MoveDirection direction) {
        boolean fieldStateChanged = false;

        rotateFieldDown(direction, false);
        if (allTilesDown(fieldStateChanged)) { fieldStateChanged = true; }
        if (mergeTiles(fieldStateChanged))   { fieldStateChanged = true; }
        if (allTilesDown(fieldStateChanged)) { fieldStateChanged = true; }
        rotateFieldDown(direction, true);

        if (fieldStateChanged) {
            //the larger the field, the more new tiles are generated
            for (int i = 0; i < rowCount-3; i++) {
                initializeNewTile();
            }
        }
        updateGameState();
    }

    private void rotateFieldDown(MoveDirection direction, boolean rotateBack) {
        if (direction == MoveDirection.UP) {
            turnRightField();
            turnRightField();
        }
        else if (direction == MoveDirection.RIGHT) {
            if (!rotateBack){
                turnRightField();
            }
            else {
                turnRightField();
                turnRightField();
                turnRightField();
            }
        }
        else if (direction == MoveDirection.LEFT){
            if (rotateBack) {
                turnRightField();
            }
            else {
                turnRightField();
                turnRightField();
                turnRightField();
            }
        }
    }

    private void turnRightField() {
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
        int canBeMergedCount = 0;

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (tiles[i][j].getValue() == 1024) { setState(FieldState.WON); }
                if (tiles[i][j].getValue() == 0) { emptyTilesCount++; }
                canBeMergedCount = getCanBeMergedCount(canBeMergedCount, i, j);
            }
        }

        if (getState() != FieldState.WON) {
            if (emptyTilesCount == 0 && canBeMergedCount == 0) {
                setState(FieldState.LOST);
            }
            else {
                setState(FieldState.PLAYING);
            }
        }
    }

    private int getCanBeMergedCount(int canBeMergedCount, int i, int j) {
        if (j < columnCount - 1) {
            if (tiles[i][j].getValue() == tiles[i][j + 1].getValue()) {
                canBeMergedCount++;
            }
        }
        if (i < rowCount - 1) {
            if (tiles[i][j].getValue() == tiles[i +1][j].getValue()) {
                canBeMergedCount++;
            }
        }
        return canBeMergedCount;
    }


    //create field and initialize it by zeros and 2 non-zeros tiles for game start
    private void generateField() {
        initializeField();
        initializeNewTile();
        initializeNewTile();
    }

    private void initializeField() {
        tiles = new Tile[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                tiles[i][j] = new Tile(0);
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
            if (tiles[randomRow][randomColumn].getValue() == 0) {
                tiles[randomRow][randomColumn].setValue(rand.nextInt(2)+1); //initialize tile by 1 or 2
                isAlreadyInitialized = true;
            }
        }
    }

    public void resetGameData() {
        generateField();
        state = FieldState.PLAYING;
        score = 0;
    }
}
