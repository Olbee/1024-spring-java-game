import game1024.consoleui.ConsoleUI;
import game1024.core.*;

public class Main {
    public static void main(String[] args) {

        Field field = new Field(5, 5);
        ConsoleUI ui = new ConsoleUI(field);
        ui.play();

    }
}
