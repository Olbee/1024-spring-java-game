import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import game1024.core.Field;
import game1024.core.FieldState;

import java.util.Random;

public class FieldTest {

    private Random randomGenerator = new Random();
    Field field;
    private final int rowCount;
    private final int columnCount;

    public FieldTest() {
        this.rowCount = randomGenerator.nextInt(10) + 4;
        this.columnCount = rowCount;
        field = new Field(rowCount, columnCount);
    }

    @Test
    public void generatedFieldCheck() {
        int zerosCount = 0;
        int nonzeroCount = 0;

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (field.getTile(i, j).getValue() == 0) {
                    zerosCount++;
                }
                else if (field.getTile(i, j).getValue() == 1 || field.getTile(i, j).getValue() == 2) {
                    nonzeroCount++;
                }
            }
        }
        Assertions.assertEquals(rowCount * columnCount - 2, zerosCount); //all zeros minus 2 initialized tiles
        Assertions.assertEquals(2, nonzeroCount);
    }

    @Test
    public void fieldWithTooSmallSize() {
        Field fieldWithTooSmallSize = null;
        int tooSmallSize = 3;
        try {
            fieldWithTooSmallSize = new Field(3, 3);
        } catch(Exception e) {
            // field less than 4x4 should not be created - it may fail on exception
        }
        Assertions.assertTrue(fieldWithTooSmallSize == null || fieldWithTooSmallSize.getRowCount() > 3);
    }

    @Test
    public void notASquareField() {
        Field notASquareField = null;
        try {
            notASquareField = new Field(6, 5);
        } catch(Exception e) {
            // not a square field should not be created - it may fail on exception
        }
        Assertions.assertTrue(notASquareField == null);
    }

    @Test
    public void getRowCountCheck() {
        Assertions.assertEquals(rowCount, field.getRowCount());
    }

    @Test
    public void getColumnCountCheck() {
        Assertions.assertEquals(columnCount, field.getColumnCount());
    }

    @Test
    public void getStateCheck() {
        Assertions.assertEquals(FieldState.PLAYING, field.getState());
    }

    @Test
    public void getScoreCheck() {
        Assertions.assertEquals(0, field.getScore());
    }

   /* @Test
    public void gameLost() {
        //? ? ?
    }
    */

    @Test
    public void resetGameData() {
        generatedFieldCheck();
        Assertions.assertEquals(FieldState.PLAYING, field.getState());
        Assertions.assertEquals(0, field.getScore());
    }
}
