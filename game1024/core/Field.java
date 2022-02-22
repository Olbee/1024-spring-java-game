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
        //2 not empty tiles for start of the game
        initializeNewTile();
        initializeNewTile();
    }

    public FieldState getState() {
        return state;
    }

    void setState(FieldState state) {
        this.state = state;
    }

    public void moveTiles(MoveDirection direction) {
        if (direction == MoveDirection.DOWN) {

        }
    }

    public void drawField(){
        System.out.println();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                System.out.print("  " + tiles[i][j].getValue() + " ");
            }
            System.out.println();
        }
    }

    private void initializeNewTile() {
        Random rand = new Random();
        int randomRow;
        int randomColumn;
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

    //create and initialize field with zeros
    private void generateField() {
        tiles = new Tile[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                tiles[i][j] = new Tile(0);
            }
        }
    }
}
