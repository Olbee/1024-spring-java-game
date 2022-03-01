package game1024.consoleui;

import game1024.core.Field;
import game1024.core.FieldState;
import game1024.core.MoveDirection;

import java.util.Locale;
import java.util.Scanner;

public class ConsoleUI {
    private final Field field;
    private Scanner scanner = new Scanner(System.in);

    public ConsoleUI(Field field) {
        this.field = field;
    }

    public void play() {
        while (field.getState() == FieldState.PLAYING) {
            drawField();
            inputProcess();
        }
        if (field.getState() == FieldState.WON) {
            System.out.println("Congratulation!");
        }
        else {
            System.out.println("Game Over");
        }
    }

    private void drawField(){
        System.out.println("score: " + field.getScore());
        for (int i = 0; i < field.getRowCount(); i++) {
            for (int j = 0; j < field.getColumnCount(); j++) {
                System.out.print("  " + field.getTile(i, j).getValue() + " ");
            }
            System.out.println("\n");
        }
    }

    private void inputProcess(){
        System.out.println("Write command:\nX - exit\n\nW - move UP\nS - move DOWN\nA - move LEFT\nD - move RIGHT");

        char input = scanner.next().toUpperCase().charAt(0);

        switch(input) {
            case 'W': {
                field.moveTiles(MoveDirection.UP);
                break;
            }
            case 'S': {
                field.moveTiles(MoveDirection.DOWN);
                break;
            }
            case 'A': {
                field.moveTiles(MoveDirection.LEFT);
                break;
            }
            case 'D': {
                field.moveTiles(MoveDirection.RIGHT);
                break;
            }
            case 'X': {
                System.exit(0);
                break;
            }
            default: {
                System.out.println("Invalid input.");
                break;
            }
        }
        field.updateGameState();
    }

}
