import game1024.core.*;

public class Main {
    public static void main(String[] args) {

        Field field = new Field(4, 4);

        field.drawField();
        while (field.getState() == FieldState.PLAYING) {

            field.moveTiles(MoveDirection.DOWN);
            field.drawField();
            field.updateGameState();
        }

        System.out.println("You " + field.getState());

    }
}
