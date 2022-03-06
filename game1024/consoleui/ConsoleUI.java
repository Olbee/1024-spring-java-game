package game1024.consoleui;

import game1024.core.Field;
import game1024.core.FieldState;
import game1024.core.MoveDirection;

import java.util.Scanner;

public class ConsoleUI {

    public enum Colors {
        DEFAULT ("\u001B[0m"),
        RED ("\u001B[31m"),
        GREEN ("\u001B[32m"),
        YELLOW ("\u001B[33m"),
        BLUE ("\u001B[34m"),
        WHILE ("\u001B[37m");

        private final String pattern;
        Colors(String pattern) { this.pattern = pattern; }

        @Override
        public String toString() {
            return pattern;
        }

    }

    private final Field field;
    private final Scanner scanner;

    public ConsoleUI(Field field) {
        this.field = field;
        this.scanner = new Scanner(System.in);
    }

    public void play() {

        do { drawField(); handleUserInput(); }
        while (field.getState() == FieldState.PLAYING);

        drawField();

        if (field.getState() == FieldState.WON) { System.out.println("Congratulation!"); }
        else if (field.getState() == FieldState.LOST) { System.out.println("Game Over"); }

        QuestionToRestartGame();
    }

    private void QuestionToRestartGame() {
        System.out.println("Want to play again? (A/N)?");
        char input;
        while (true) {
            input = scanner.next().toUpperCase().charAt(0);
            if (input == 'Y') {
                field.resetGameData();
                play();
            }
            else if (input == 'N') {
                scanner.close();
                System.exit(0);
            }
        }
    }

    private void handleUserInput(){
        System.out.println("Write command:\n" + "X - exit\n" + "W - move UP\n" +
                "S - move DOWN\n" + "A - move LEFT\n" + "D - move RIGHT");
        moveByPressedKey();
    }

    private void moveByPressedKey() {
        char input;
        boolean inCorrectInput = true; //to enter while cykle

        while (inCorrectInput){
            input = scanner.next().toUpperCase().charAt(0);

            switch (input) {

                case 'W' : { field.moveTiles(MoveDirection.UP); inCorrectInput = false; break; }
                case 'S' : { field.moveTiles(MoveDirection.DOWN); inCorrectInput = false; break; }
                case 'A' : { field.moveTiles(MoveDirection.LEFT); inCorrectInput = false; break; }
                case 'D' : { field.moveTiles(MoveDirection.RIGHT); inCorrectInput = false; break; }
                case 'X' : { scanner.close(); System.exit(0); break; }
                default : {
                    System.out.println("incorrect input, try again");
                    inCorrectInput = true;
                    break;
                }
            }
        }
    }

    private void drawField(){
        drawScore();
        drawBox();
    }

    private void drawScore() {
        System.out.println("score: " + field.getScore());
    }

    private void drawBox() {
        for (int i = 0; i < field.getRowCount(); i++) {
            multiplyPrintByPattern("|----", field.getRowCount());
            System.out.println("|");
            for (int j = 0; j < field.getColumnCount(); j++) {
                printColoredValue(i, j);
            }
            System.out.println("|");
        }
        multiplyPrintByPattern("|----", field.getRowCount());
        System.out.println("|" + "\n");
    }

    private void multiplyPrintByPattern(String pattern, int n) {
        for (int i = 0; i < n; i++) {
            System.out.print(pattern);
        }
    }

    private void printColoredValue (int i, int j) {
        if (field.getTile(i, j).getValue() == 0) {
            System.out.printf("|%s%-4s%s", Colors.WHILE, field.getTile(i, j), Colors.DEFAULT);
        }
        else if (field.getTile(i, j).getValue() < 16) {
            System.out.printf("|%s%-4s%s", Colors.BLUE, field.getTile(i, j), Colors.DEFAULT);
        }
        else if (field.getTile(i, j).getValue() < 128) {
            System.out.printf("|%s%-4s%s", Colors.GREEN, field.getTile(i, j), Colors.DEFAULT);
        }
        else if (field.getTile(i, j).getValue() < 512) {
            System.out.printf("|%s%-4s%s", Colors.RED, field.getTile(i, j), Colors.DEFAULT);
        }
        else if (field.getTile(i, j).getValue() == 1024) {
            System.out.printf("|%s%-4s%s", Colors.YELLOW, field.getTile(i, j), Colors.DEFAULT);
        }
    }

}
