package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.io.FileInputStream;

public class Preloader extends javafx.application.Preloader {
    @Override
    public void start(Stage stage) {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setMinWidth(200);
        Font font = null;
        try (FileInputStream fileInputStream = new FileInputStream("nsjpl.otf")) {
            font = Font.loadFont(fileInputStream, 16);
        } catch (Exception e) { e.printStackTrace(); }
        this.font = font;
        Label label = new Label();
        label.setMinHeight(10);
        VBox vBox = new VBox(getCenteredLabel(MainWindow.TITLE),
                getCenteredLabel("「30k japanese words with sample sentences」"),
                getCenteredLabel("by tfiamietsh"), label, progressBar);
        vBox.setAlignment(Pos.CENTER);
        HBox hBox = new HBox(vBox);
        hBox.setAlignment(Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(hBox);
        stage.setScene(new Scene(borderPane, Math.divideExact(MainWindow.WIDTH, 2),
                Math.divideExact(MainWindow.HEIGHT, 4)));
        stage.show();
        this.stage = stage;
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
        if (stateChangeNotification.getType() == StateChangeNotification.Type.BEFORE_START)
            this.stage.hide();
    }

    private Label getCenteredLabel(String text) {
        Label label = new Label(text);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(this.font == null ? label.getFont() : this.font);
        return label;
    }

    private Font font;
    private Stage stage;
}
