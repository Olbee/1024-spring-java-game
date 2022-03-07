import game1024.consoleui.ConsoleUI;
import game1024.core.*;

import java.io.*;

public class Main {
    public static void main(String[] args) {

        int rowCount = getUserRowAndColumnInput();
        int columnCount = rowCount;

        Field field = new Field(rowCount, columnCount);
        ConsoleUI ui = new ConsoleUI(field);
        ui.play();
    }

    private static int getUserRowAndColumnInput() {
        System.out.println("Write the size of field you want to play.\n" +
                "For example: 4 to play 4x4.\n" + "" +
                "Note: You cannot create a field with size less than 4x4.\n");

        String input = null;
        int number = 0;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        boolean correctInput = false;

        while (!correctInput) {
            try {
                input = bufferedReader.readLine();
                number = Integer.parseInt(input);
                correctInput = true;
            } catch (NumberFormatException ex) {
                System.out.println("Your input is not a number!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return number;
    }
}
