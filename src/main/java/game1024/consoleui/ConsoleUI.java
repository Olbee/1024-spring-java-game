package game1024.consoleui;

import game1024.core.*;
import game1024.entity.*;
import game1024.service.comment.*;
import game1024.service.rating.*;
import game1024.service.score.*;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {

    public enum Colors {
        DEFAULT ("\u001B[0m"),
        RED ("\u001B[31m"),
        GREEN ("\u001B[32m"),
        YELLOW ("\u001B[33m"),
        BLUE ("\u001B[34m"),
        PURPLE ("\u001B[35m"),
        WHILE ("\u001B[37m");

        private final String pattern;

        Colors(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public String toString() {
            return pattern;
        }
    }

    private Field field;
    private final Scanner scanner;
    private static final Pattern INPUT_PATTERN = Pattern.compile("([4-9])x([4-9])");
    private final ScoreService scoreService = new ScoreServiceJDBC();
    private final RatingService ratingService = new RatingServiceJDBC();
    private final CommentService commentService = new CommentServiceJDBC();

    public ConsoleUI() throws IOException {
        this.scanner = new Scanner(System.in);
        int size = getFieldSize();
        field = new Field(size, size);
    }

    public void playGame() throws IOException {

        while (field.getState() == FieldState.PLAYING) {
            drawField();
            handleUserInput();
        }

        drawField();

        if (field.getState() == FieldState.WON) {
            System.out.println("Congratulation, you won!\n");
        }
        else if (field.getState() == FieldState.LOST) {
            System.out.println("Game Over\n");
        }

        executeGameService();
        askToPlayAgain();
    }

    private void executeGameService() {
        saveGameScore();
        printTopScores();

        commentGame();
        printLastComments();

        printGameAverageRating();
        printUserLastGameRate();
        setGameRating();
    }

    public void printGameAverageRating() {
        boolean wantToPrintGameAverageRating = askUserYesOrNo("Do you want to print average user rating of this game? (Y/N)\n");
        if (wantToPrintGameAverageRating) {
            System.out.println("Average user rating of this game is: " + ratingService.getAverageRating("1024") + "\n");
        }
    }

    public void printUserLastGameRate() {
        boolean wantToPrintLastRatings = askUserYesOrNo("Do you want to print how you rated this game last time? (Y/N)\n");
        if (wantToPrintLastRatings) {
            int grade = ratingService.getRating("1024", (System.getProperty("user.name")));
            if (grade > 0 && grade <= 5) {
                System.out.println("Last time you rated this game on: " + grade + "\n");
            }
            else {
                System.out.println("You have not rated this game yet");
            }
        }
    }

    private void setGameRating () {
        boolean wantToRateGame = askUserYesOrNo("Do you want to rate this game? (Y/N)");
        if (wantToRateGame) {
            System.out.print("\nHow do you rate this game? (1-5): ");
            int grade = -1; //just to enter while cycle
            while (grade < 0 || grade > 5) {
                grade = scanner.nextInt();
                scanner.nextLine();
                if (grade < 0 || grade > 5) System.out.println("try again");
            }
            ratingService.setRating(new Rating("1024", System.getProperty("user.name"), new Date(), grade));
        }
    }

    public void printLastComments() {
        boolean wantToPrintLastComments = askUserYesOrNo("\nDo you want to print last comments of this game? (Y/N)");
        if (wantToPrintLastComments) {
            List<Comment> comments = commentService.getComments("1024");
            multiplyPrintByPattern("--", field.getRowCount() * field.getColumnCount());
            System.out.println();
            for (int i = 0; i < comments.size(); i++) {
                System.out.println(comments.get(i).getPlayer() + "   :   " + comments.get(i).getComment()
                                                      + "   at   " + comments.get(i).getCommented_on());
            }
            multiplyPrintByPattern("--", field.getRowCount() + field.getColumnCount());
            System.out.println("\n");
        }
    }

    private void commentGame () {
        boolean wantToCommentGame = askUserYesOrNo("Do you want to comment this game? (Y/N)\n");
        if (wantToCommentGame) {
            System.out.print("Write your comment: ");
            String text = scanner.nextLine();
            commentService.addComment(new Comment("1024", System.getProperty("user.name"), new Date(), text));
        }
    }

    private void saveGameScore() {
        System.out.println("You scored " + field.getScore() + " points!\n");
        scoreService.addScore(new Score("1024", System.getProperty("user.name"), new Date(), field.getScore()));
    }

    private void printTopScores() {
        boolean wantToPrintTopScores = askUserYesOrNo("Do you want to print top scores of this game? (Y/N)\n");
        if (wantToPrintTopScores) {
            List<Score> scores = scoreService.getTopScores("1024");
            multiplyPrintByPattern("--", field.getRowCount() * field.getColumnCount());
            System.out.println();
            for (int i = 0; i < scores.size(); i++) {
                System.out.println(scores.get(i).getPlayer() + "   :   " + scores.get(i).getPoints()
                        +" points   at   " + scores.get(i).getPlayed_on());
            }
            multiplyPrintByPattern("--", field.getRowCount() * field.getColumnCount());
            System.out.println("\n");
        }
    }

    private int getFieldSize() throws IOException {
        System.out.println("Write the field size you want to play.\n\n" +
                "For example: 4x4, 5x5, 6x6..\n" + "" +
                "Note: 4x4 is the smallest possible size of the field\n" +
                "Note: 9x9 is the biggest possible size of the field\n" +
                "Note: You cannot create field with different size of row and column (F.e. 4x5)\n");
        return handleFieldSizeInput();
    }

    private int handleFieldSizeInput() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        boolean correctInput = false;

        String input = null;
        int fieldSize = 0;

        while (!correctInput) {
            System.out.print("Field: ");
            input = bufferedReader.readLine();
            Matcher matcher = INPUT_PATTERN.matcher(input);
            if (matcher.matches()) {
                if (input.charAt(0) == input.charAt(2)) { // 4x4 -> 4 == 4
                    fieldSize = input.charAt(0) - 48; //ascii symbol to dec (char to int)
                    correctInput = true;
                }
                else {
                    System.out.println("You cannot create a field of different sizes");
                }
            }
            else {
                System.out.println("invalid input.");
            }
        }
        return fieldSize;
    }


    private void askToPlayAgain() throws IOException {
        boolean wantToPlayAgain = askUserYesOrNo("\nDo you want to play again? (Y/N)");
        if (wantToPlayAgain) {
            field.resetGameData();
            int newSize = getFieldSize();
            field = new Field(newSize, newSize);
            playGame();
        }
        else {
            scanner.close();
            System.exit(0);
        }
    }

    private boolean askUserYesOrNo(String text) { //return TRUE if YES, and FALSE if NO
        System.out.println(text);
        char input;
        while (true) {
            input = scanner.next().toUpperCase().charAt(0);
            scanner.nextLine();
            if (input == 'Y') { return true; }
            else if (input == 'N') { return false; }
            System.out.println("invalid input.");
            System.out.println(text);
        }
    }

    private void handleUserInput(){
        System.out.println("Write command:\n" + "X - exit\n" + "W - move UP\n" +
                "S - move DOWN\n" + "A - move LEFT\n" + "D - move RIGHT");
        handleMoveInput();
    }

    private void handleMoveInput() {
        char input;
        boolean inCorrectInput = true; //true -> to enter while cykle

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
        System.out.printf("%s score: %d %s\n", Colors.PURPLE, field.getScore(), Colors.DEFAULT);
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
            System.out.printf("|%s    %s", Colors.WHILE, Colors.DEFAULT);
        }
        else if (field.getTile(i, j).getValue() < 16) {
            System.out.printf("| %s%-3s%s", Colors.BLUE, field.getTile(i, j), Colors.DEFAULT);
        }
        else if (field.getTile(i, j).getValue() < 128) {
            System.out.printf("| %s%-3s%s", Colors.GREEN, field.getTile(i, j), Colors.DEFAULT);
        }
        else if (field.getTile(i, j).getValue() < 512) {
            System.out.printf("|%s%-4s%s", Colors.RED, field.getTile(i, j), Colors.DEFAULT);
        }
        else if (field.getTile(i, j).getValue() == 1024) {
            System.out.printf("|%s%-4s%s", Colors.YELLOW, field.getTile(i, j), Colors.DEFAULT);
        }
    }

}
