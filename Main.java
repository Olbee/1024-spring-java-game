import game1024.consoleui.ConsoleUI;
import game1024.core.*;
import org.junit.jupiter.api.Test;

public class Main {
    public static void main(String[] args) {

        Field field = new Field(4, 4);
        ConsoleUI ui = new ConsoleUI(field);
        ui.play();
    }
}
