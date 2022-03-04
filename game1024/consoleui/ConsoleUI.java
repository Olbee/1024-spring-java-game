package game1024.consoleui;

import game1024.core.Field;
import game1024.core.FieldState;
import game1024.core.MoveDirection;

import java.util.Scanner;

public class ConsoleUI {

    private final Field field;
    private Scanner scanner;

    public ConsoleUI(Field field) {
        this.field = field;
        this.scanner = new Scanner(System.in);
    }

    public void play() {
        do {
            drawField();
            inputProcess();
        }
        while (field.getState() == FieldState.PLAYING);

        drawField();
        if (field.getState() == FieldState.WON) { System.out.println("Congratulation!"); }
        else if (field.getState() == FieldState.LOST) { System.out.println("Game Over"); }
        restartGame();

    }

    private void restartGame() {
        System.out.println("Want to play again? (A/N)?");
        char input;
        while (true) {
            input = scanner.next().toUpperCase().charAt(0);
            if (input == 'A') {
                field.resetGameData();
                play();
            }
            else if (input == 'N') {
                break;
            }
        }
    }

    private void drawField(){
        for (int i = 0; i < field.getRowCount(); i++) {
            for (int j = 0; j < field.getColumnCount(); j++) {
                System.out.print("  " + field.getTile(i, j).getValue() + " ");
            }
            System.out.println("\n");
        }
        System.out.println("\tscore: " + field.getScore() + "\n");
    }

    private void inputProcess(){
        System.out.println("Write command:\n" + "X - exit\n" + "W - move UP\n" +
                                "S - move DOWN\n" + "A - move LEFT\n" + "D - move RIGHT");
        moveByPressedKey();
    }

    private void moveByPressedKey() {
        char input = scanner.next().toUpperCase().charAt(0);
        switch (input) {
            case 'W': { field.moveTiles(MoveDirection.UP); break; }
            case 'S': { field.moveTiles(MoveDirection.DOWN); break; }
            case 'A': { field.moveTiles(MoveDirection.LEFT); break; }
            case 'D': { field.moveTiles(MoveDirection.RIGHT); break; }
            case 'X': { System.exit(0); break; }
        }
    }

}
