package gamestudio.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TileTest {

    private Tile tile = new Tile(0);

    @Test
    public void getValueTest() {
        tile.setValue(2);
        assertEquals(2, tile.getValue());
    }

    @Test
    public void setValueTest() {
        tile.setValue(5);
        assertEquals(5, tile.getValue());
    }

    @Test
    public void toStringValueTest() {
        tile.setValue(3);
        assertEquals("3", tile.toString());
    }

}
