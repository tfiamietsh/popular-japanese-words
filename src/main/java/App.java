import ui.MainWindow;
import ui.Preloader;

public class App {
    public static void main(String[] args) {
        System.setProperty("javafx.preloader", Preloader.class.getName());
        MainWindow.main(args);
    }
}
