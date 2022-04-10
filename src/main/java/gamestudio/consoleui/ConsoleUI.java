package gamestudio.consoleui;


import gamestudio.core.*;
import gamestudio.entity.*;
import gamestudio.service.account.AccountService;
import gamestudio.service.comment.*;
import gamestudio.service.rating.*;
import gamestudio.service.score.*;

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
    private String userName;
    private static final Pattern INPUT_PATTERN = Pattern.compile("([4-9])(x|X)([4-9])");

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private AccountService accountService;

    public ConsoleUI() throws IOException {
        this.scanner = new Scanner(System.in);
        int size = getFieldSize();
        field = new Field(size, size);
    }

    public void playGame() throws IOException {

        commentService.addComment(new Comment("1024", "Ihore4ek", new Date(), "LOL"));

        handleUserLogin();

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

    private void handleUserLogin() {
        System.out.print("---------------------");
        System.out.println("\nChoose one of the following options: ");
        System.out.println("1. play as a guest.");
        System.out.println("2. sign in.");
        System.out.println("3. register an account.");
        System.out.println("x. exit.");
        System.out.println("---------------------");

        char input;
        boolean inCorrectInput = true; //true -> to enter while cykle

        while (inCorrectInput){
            input = scanner.next().toUpperCase().charAt(0);
            switch (input) {
                case '1': {
                    userName = "guest";
                    inCorrectInput = false;
                    break;
                }
                case '2': {
                    scanner.nextLine();
                    accountLogin();
                    inCorrectInput = false;
                    break;
                }
                case '3': {
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
        }
        else {
            System.out.println("Wrong login or password. Please try again..\n");
            handleUserLogin();
        }
    }

    private void accountRegistration() {
            System.out.print("Write new login: ");
            String login = scanner.nextLine();
            System.out.print("Write new password: ");
            String password = scanner.nextLine();
            accountService.addAccount(new Account("1024", login, password));
            System.out.println("Your account has been successfully registered");
    }

    private void executeGameService() {
        saveGameScore();
        char input;

        while (true){
            System.out.print("---------------------");
            System.out.println("\nChoose one of the following options: ");
            System.out.println("1. comment game.");
            System.out.println("2. set game rating.");
            System.out.println("3. print my latest game rating.");
            System.out.println("4. print top scores.");
            System.out.println("5. print last comments.");
            System.out.println("6. print average game rating.");
            System.out.println("x. exit.");
            System.out.println("---------------------");

            input = scanner.next().toUpperCase().charAt(0);
            switch (input) {
                case '1' : { commentGame(); break; }
                case '2' : { setGameRating(); break; }
                case '3' : { printUserLastGameRate(); break; }
                case '4' : { printTopScores(); break; }
                case '5' : { printLastComments(); break; }
                case '6' : { printGameAverageRating(); break; }
                case 'X' : { scanner.close(); System.exit(0); break; }
                default : { System.out.println("incorrect input, try again"); break; }
            }
        }
    }

    public void printGameAverageRating() {
        System.out.println("Average user rating of this game is: " + ratingService.getAverageRating("1024") + "\n");

    }

    public void printUserLastGameRate() {
        int grade = (int) ratingService.getRating("1024", userName);
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
