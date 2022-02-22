package game1024.core;

import java.util.Random;

public class Field {
    private Tile[][] tiles;
    private final int rowCount;
    private final int columnCount;
    private FieldState state;
    private static int score = 0;


    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;

        GenerateField();
    }

    public FieldState getState() {
        return state;
    }

    void setState(FieldState state) {
        this.state = state;
    }

    public void moveTiles(MoveDirection direction) {

    }

    private void GenerateField() {
        tiles = new Tile[rowCount][columnCount];
        //Random rand = new Random();
        //int FieldBound = (rows * columns)-1; // we have always 4x4 field

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                tiles[i][j] = new Tile(0);
                System.out.print(tiles[i][j].getValue());
            }
            System.out.println();
        }

    }
}
