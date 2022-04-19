package sk.tuke.gamestudio.consoleui;


import sk.tuke.gamestudio.core.*;
import sk.tuke.gamestudio.entity.*;
import sk.tuke.gamestudio.service.account.AccountService;
import sk.tuke.gamestudio.service.comment.*;
import sk.tuke.gamestudio.service.rating.*;
import sk.tuke.gamestudio.service.score.*;

import org.springframework.beans.factory.annotation.Autowired;

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
        WHILE ("\u001B[37m"),

        BLACK_BACKGROUND ("\u001B[40m");

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
    private String userName;
    private boolean signed_in;
    private int level;

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private AccountService accountService;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        level = handleGameLevel();
        field = new Field(4, 4, level);
        signed_in = false;
    }

    public void playGame() throws IOException {

        if (!signed_in) {
            handleUserLogin();
        }



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
    }

    private void handleUserLogin() {
        System.out.print("---------------------");
        System.out.println("\nChoose one of the following options: ");
        System.out.println("1. login.");
        System.out.println("2. register an account.");
        System.out.println("x. exit");
        System.out.println("---------------------");

        char input;
        boolean inCorrectInput = true; //true -> to enter while cykle

        while (inCorrectInput){
            input = scanner.next().toUpperCase().charAt(0);
            switch (input) {
                case '1': {
                    scanner.nextLine();
                    accountLogin();
                    inCorrectInput = false;
                    break;
                }
                case '2': {
                    scanner.nextLine();
                    accountRegistration();
                    handleUserLogin();
                    inCorrectInput = false;
                    break;
                }
                case 'X': {
                    scanner.close();
                    System.exit(0);
                    break;
                }
                default: {
                    System.out.println("incorrect input, try again");
                    inCorrectInput = true;
                    break;
                }
            }
        }
    }

    private void accountLogin() {
        System.out.print("Write your login: ");
        String login = scanner.nextLine();
        System.out.print("Write your password: ");
        String password = scanner.nextLine();

        String expectedPassword = accountService.getAccountPassword("1024", login);

        if (password.equals(expectedPassword)) {
            userName = login;
            signed_in = true;
        }
        else {
            System.out.println("Wrong login or password. Please try again..\n");
            handleUserLogin();
        }
    }

    private void accountRegistration() {
            System.out.print("Write new login: ");
            String login = scanner.nextLine();
            System.out.print("Write new email: ");
            String email = scanner.nextLine();
            System.out.print("Write new password: ");
            String password = scanner.nextLine();
            accountService.addAccount(new Account("1024", email, login, password));
            System.out.println("Your account has been successfully registered");
    }

    private void executeGameService() throws IOException {
        saveGameScore();
        char input;

        while (true){
            System.out.print("---------------------");
            System.out.println("\nChoose one of the following options: ");
            System.out.println("1. play again.");
            System.out.println("2. comment game.");
            System.out.println("3. set game rating.");
            System.out.println("4. print my latest game rating.");
            System.out.println("5. print top scores.");
            System.out.println("6. print last comments.");
            System.out.println("7. print average game rating.");
            System.out.println("x. exit.");
            System.out.println("---------------------");

            input = scanner.next().toUpperCase().charAt(0);
            switch (input) {
                case '1' : { playAgain(); break; }
                case '2' : { commentGame(); break; }
                case '3' : { setGameRating(); break; }
                case '4' : { printUserLastGameRate(); break; }
                case '5' : { printTopScores(); break; }
                case '6' : { printLastComments(); break; }
                case '7' : { printGameAverageRating(); break; }
                case 'X' : { scanner.close(); System.exit(0); break; }
                default : { System.out.println("incorrect input, try again"); break; }
            }
        }
    }

    public void printGameAverageRating() {
        int avgRating = ratingService.getAverageRating("1024");
        if (avgRating == 0) {
            System.out.println("This game has not yet been rated by anyone.");
        }
        else {
            System.out.println("Average user rating of this game is: " + avgRating + "\n");
        }
    }

    public void printUserLastGameRate() {
        int grade = ratingService.getRating("1024", userName);
        if (grade > 0 && grade <= 5) {
            System.out.println("Last time you rated this game on: " + grade + "\n");
        } else {
            System.out.println("You have not rated this game yet");
        }
    }

    private void setGameRating() {
        System.out.print("\nHow do you rate this game? (1-5): ");
        int grade = -1; //just to enter while cycle
        while (grade < 0 || grade > 5) {
            grade = scanner.nextInt();
            scanner.nextLine();
            if (grade < 0 || grade > 5) System.out.println("try again");
        }
        ratingService.setRating(new Rating("1024", userName, new Date(), grade));
    }

    public void printLastComments() {
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

    private void commentGame() {
        scanner.nextLine();
        System.out.print("Write your comment: ");
        String text = scanner.nextLine();
        commentService.addComment(new Comment("1024", userName, new Date(), text));
    }

    private void saveGameScore() {
        System.out.println("You scored " + field.getScore() + " points!\n");
        scoreService.addScore(new Score("1024", userName, new Date(), field.getScore()));
    }

    private void printTopScores() {
        List<Score> scores = scoreService.getTopScores("1024");
        multiplyPrintByPattern("--", field.getRowCount() * field.getColumnCount());
        System.out.println();
        for (int i = 0; i < scores.size(); i++) {
            System.out.println(scores.get(i).getPlayer() + "   :   " + scores.get(i).getPoints()
                    + " points   at   " + scores.get(i).getPlayed_on());
        }
        multiplyPrintByPattern("--", field.getRowCount() * field.getColumnCount());
        System.out.println("\n");
    }

    private int handleGameLevel() {
        char input;

        while (true){
            System.out.println("\nWrite command: ");
            System.out.println("1. to play easy level.");
            System.out.println("2. to play hard level.");
            System.out.println("B. to sign out.\n");

            input = scanner.next().toUpperCase().charAt(0);
            switch (input) {
                case '1' : { return 1; }
                case '2' : { return 2; }
                case 'B' : { signed_in = false; return 0; }
                default : { System.out.println("incorrect input, try again"); break; }
            }
        }
    }

    private void playAgain() throws IOException {
            field.resetGameData();
            int level = handleGameLevel();
            field = new Field(4, 4, level);
            playGame();

    }

    private void handleUserInput() throws IOException {
        System.out.println("\nWrite command:");
        System.out.println("W - move UP.");
        System.out.println("S - move DOWN.");
        System.out.println("A - move LEFT.");
        System.out.println("D - move RIGHT.");
        System.out.println("N - start new game.");
        System.out.println("X - exit.");

        handleMoveInput();
    }

    private void handleMoveInput() throws IOException {
        char input;
        boolean inCorrectInput = true; //true -> to enter while cykle

        while (inCorrectInput){
            input = scanner.next().toUpperCase().charAt(0);
            switch (input) {
                case 'W' : { field.moveTiles(MoveDirection.UP); inCorrectInput = false; break; }
                case 'S' : { field.moveTiles(MoveDirection.DOWN); inCorrectInput = false; break; }
                case 'A' : { field.moveTiles(MoveDirection.LEFT); inCorrectInput = false; break; }
                case 'D' : { field.moveTiles(MoveDirection.RIGHT); inCorrectInput = false; break; }
                case 'N' : { playAgain(); inCorrectInput = false; break; }
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
        System.out.printf("%s %s -> score: %d %s\n", Colors.PURPLE, userName, field.getScore(), Colors.DEFAULT);
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
        if (field.getTile(i, j).getValue() == -1) {
            System.out.printf("|%s    %s", Colors.BLACK_BACKGROUND, Colors.DEFAULT);
        }
        else if (field.getTile(i, j).getValue() == 0) {
            System.out.printf("| %s%-3s%s", Colors.WHILE, field.getTile(i, j), Colors.DEFAULT);
        }
        else if (field.getTile(i, j).getValue() < 16) {
            System.out.printf("| %s%-3s%s", Colors.BLUE, field.getTile(i, j), Colors.DEFAULT);
        }
        else if (field.getTile(i, j).getValue() < 256) {
            System.out.printf("| %s%-3s%s", Colors.GREEN, field.getTile(i, j), Colors.DEFAULT);
        }
        else if (field.getTile(i, j).getValue() < 1024) {
            System.out.printf("| %s%-3s%s", Colors.RED, field.getTile(i, j), Colors.DEFAULT);
        }
        else if (field.getTile(i, j).getValue() == 1024) {
            System.out.printf("|%s%-4s%s", Colors.YELLOW, field.getTile(i, j), Colors.DEFAULT);
        }
    }

}
