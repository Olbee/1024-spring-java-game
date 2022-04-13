package core;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.core.Field;
import sk.tuke.gamestudio.core.FieldState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

public class FieldTest {

    private final Random randomGenerator = new Random();
    private final int rowCount = 4;
    private final int columnCount = 4;
    Field field = new Field(rowCount, columnCount);
//
//    @Test
//    public void generatedFieldCheck() {
//        int zerosCount = 0;
//        int nonzeroCount = 0;
//
//        for (int i = 0; i < rowCount; i++) {
//            for (int j = 0; j < columnCount; j++) {
//                if (field.getTile(i, j).getValue() == 0) {
//                    zerosCount++;
//                }
//                else if (field.getTile(i, j).getValue() == 1 || field.getTile(i, j).getValue() == 2) {
//                    nonzeroCount++;
//                }
//            }
//        }
//        assertEquals(rowCount * columnCount - 2, zerosCount); //all zeros minus 2 initialized tiles
//        assertEquals(2, nonzeroCount);
//    }
//
//    @Test
//    public void fieldWithTooSmallSize() {
//        Field fieldWithTooSmallSize = null;
//        int tooSmallSize = 3;
//        try {
//         //   fieldWithTooSmallSize = new Field(3, 3);
//        } catch(Exception e) {
//            // field less than 4x4 should not be created - it may fail on exception
//        }
//        assertTrue(fieldWithTooSmallSize == null || fieldWithTooSmallSize.getRowCount() > 3);
//    }
//
//    @Test
//    public void notASquareField() {
//        Field notASquareField = null;
//        try {
//            //notASquareField = new Field(6, 5);
//        } catch(Exception e) {
//            // not a square field should not be created - it may fail on exception
//        }
//        assertTrue(notASquareField == null);
//    }

    @Test
    public void getRowCountCheck() {
        assertEquals(rowCount, field.getRowCount());
    }

    @Test
    public void getColumnCountCheck() {
        assertEquals(columnCount, field.getColumnCount());
    }

    @Test
    public void getStateCheck() {
        assertEquals(FieldState.PLAYING, field.getState());
    }

    @Test
    public void getScoreCheck() { assertEquals(0, field.getScore());
    }
//
//    @Test
//    public void resetGameDataTest() {
//        generatedFieldCheck();
//        assertEquals(FieldState.PLAYING, field.getState());
//        assertEquals(0, field.getScore());
//    }
}
