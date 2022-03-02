package game1024.core;

public class Tile {
    private int value;
    private TileState state;

    public Tile(int value) {
        this.setValue(value);
        this.setState(TileState.EMPTY);
    }

    public int getValue() {
        return value;
    }

    void setValue(int value) {
        this.value = value;
        this.state = TileState.OCCUPIED; //mistake, if set value 0, it shouldn't be occupied..
    }

    public TileState getState() {
        return state;
    }

    void setState(TileState state) {
        this.state = state;
    }
}
