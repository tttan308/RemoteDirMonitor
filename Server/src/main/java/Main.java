
import view.MainScreen;

import javax.swing.*;

public class Main {
    public static MainScreen mainScreen ;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            mainScreen = new MainScreen();
        });
    }
}
