package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import mecha.Paginator;
import mecha.Pair;
import mecha.Trie;
import words.Hiragana;
import words.Word;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

public class MainWindow extends Application {
    @Override
    public void init() {
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, WIDTH, HEIGHT);

        VBox vbox = new VBox();
        this.searchTextField = new TextField();
        this.searchTextField.setMinSize(690, 25);
        this.searchTextField.setOnKeyReleased(this::onEnterReleased);
        this.searchTextField.setContextMenu(newCopyContextMenu(any -> this.searchTextField.getText()));
        Button searchButton = new Button("search");
        searchButton.setMinSize(80, 25);
        searchButton.setOnAction(e -> this.onSearch());

        this.readingLabel = new Label();
        this.readingLabel.setMinSize(380, 25);
        this.readingLabel.setAlignment(Pos.CENTER);
        this.readingLabel.setContextMenu(newCopyContextMenu(any -> this.readingLabel.getText()));
        this.translationLabel = new Label();
        this.translationLabel.setMinSize(380, 25);
        this.translationLabel.setAlignment(Pos.CENTER);
        this.translationLabel.setContextMenu(newCopyContextMenu(any -> this.translationTooltip.getText()));
        this.translationTooltip = new Tooltip();
        this.translationTooltip.setWrapText(true);
        this.translationTooltip.setMaxWidth(400);
        Duration duration = new Duration(0.);
        this.translationTooltip.setShowDelay(duration);
        this.translationTooltip.setHideDelay(duration);
        this.translationLabel.setTooltip(this.translationTooltip);

        this.listItems = FXCollections.observableArrayList();
        this.listView = new ListView<>(this.listItems);
        this.listView.setMinSize(780, 510);
        this.listView.getSelectionModel().selectedItemProperty().addListener((_1, _2, v) -> this.onSelect(v));
        this.listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(empty ? null : item);
                this.setContextMenu(empty ? null : newCopyContextMenu(any -> getText()
                        .substring(getText().indexOf('\t') + 1)));
            }
        });
        this.listView.setOnKeyPressed(this::onArrowsPressed);

        Pane pane1 = new Pane();
        pane1.setMinSize(220, 25);
        Button prevButton = new Button("prev");
        prevButton.setMinSize(60, 25);
        prevButton.setOnAction(any -> this.onPrevPage());
        this.paginatorLabel = new Label();
        this.paginatorLabel.setMinSize(210, 25);
        this.paginatorLabel.setAlignment(Pos.CENTER);
        Button nextButton = new Button("next");
        nextButton.setMinSize(60, 25);
        nextButton.setOnAction(any -> this.onNextPage());
        Pane pane2 = new Pane();
        pane2.setMinSize(100, 25);
        this.elemsPerPageSpinner = new Spinner<>(1, 30000, 120);
        this.elemsPerPageSpinner.setMinSize(80, 25);
        this.elemsPerPageSpinner.getValueFactory().valueProperty().addListener((_1, _2, v) ->
                this.onSpinnerValueChanged(v));
        Label label = new Label("per page");
        label.setMinSize(80, 25);

        vbox.getChildren().addAll(
                newPlug(),
                newHBox(newPlug(), searchTextField, newPlug(), searchButton),
                newHBox(newPlug(), readingLabel, newPlug(), translationLabel),
                newHBox(newPlug(), listView),
                newPlug(),
                newHBox(pane1, prevButton, this.paginatorLabel, nextButton, pane2,
                        this.elemsPerPageSpinner, newPlug(), label));
        borderPane.getChildren().add(vbox);

        this.paginator = new Paginator<>();
        this.hiragana = new Hiragana();
        this.selectedWord = null;
        this.trie = new Trie<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("trie"))) {
            this.trie = (Trie<Word>) ois.readObject();
        } catch(Exception e) { e.printStackTrace(); }
        this.onSearch();
        this.scene = scene;
    }

    @Override
    public void start(Stage stage) {
        stage.setScene(this.scene);
        stage.setTitle(MainWindow.TITLE);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Pane newPlug() {
        Pane pane = new Pane();
        pane.setMinSize(10, 5);
        return pane;
    }

    private HBox newHBox(Node... nodes) {
        HBox hBox = new HBox(nodes);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private ContextMenu newCopyContextMenu(Function<Void, String> getTextToCopyFunc) {
        MenuItem contextMenuItem = new MenuItem("copy");
        contextMenuItem.setOnAction(e -> Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(new StringSelection(getTextToCopyFunc.apply(null)), null));
        return new ContextMenu(contextMenuItem);
    }

    private void update() {
        this.listItems.clear();
        ArrayList<Pair<Integer, Word>> pairs = this.paginator.getCurrentPage();
        if (pairs != null) {
            for (Pair<Integer, Word> pair : pairs) {
                String word = pair.value().word();
                if (pair.value().written_using_kana_only())
                    word = String.format("%s\u3000\u3010%s\u3011", pair.value().kana(), word);
                String line = "例文：\t" + pair.value().example();
                int n = line.length();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < n; i += 62) {
                    stringBuilder.append(line, i, Math.min(i + 62, n));
                    stringBuilder.append("\n\t\t");
                }
                n = stringBuilder.length();
                stringBuilder.delete(n - 3, n);
                this.listItems.add(String.format("%d%s\n%s", pair.key(),
                        this.getLabelPrefix(pair.key()) + word,
                        stringBuilder));
            }
            this.listView.scrollTo(0);
        }
        this.paginatorLabel.setText(this.paginator.toString());
        this.listView.requestFocus();
        this.listView.getSelectionModel().select(0);
    }

    private void onSearch() {
        ArrayList<Pair<Integer, Word>> indexedWords = new ArrayList<>();
        ArrayList<Word> words = this.trie.search_all(this.searchTextField.getText());
        words.sort(Comparator.comparing(Word::hiragana));
        for (int i = 0; i < words.size(); i++)
            indexedWords.add(new Pair<>(i + 1, words.get(i)));
        this.paginator.addAll(indexedWords);
        this.update();
    }

    private void onPrevPage() {
        this.paginator.prevPage();
        this.update();
    }

    private void onNextPage() {
        this.paginator.nextPage();
        this.update();
    }

    private String getLabelPrefix(int idx) {
        String prefix = ")\t";
        if (idx < 1000)
            prefix += '\t';
        return prefix;
    }

    private void onSelect(String label) {
        if (label != null) {
            this.selectedWord = null;
            ArrayList<Pair<Integer, Word>> indexedWords = this.paginator.getCurrentPage();
            String prefix = this.getLabelPrefix(Integer.parseInt(label.substring(0, label.indexOf(")"))));
            String word = label.substring(label.indexOf(prefix) + prefix.length(), label.indexOf('\n'));
            if (word.contains("\u3000"))
                word = word.substring(word.indexOf('\u3010') + 1, word.indexOf('\u3011'));
            for (Pair<Integer, Word> pair : indexedWords)
                if (Objects.equals(pair.value().word(), word)) {
                    this.selectedWord = pair.value();
                    break;
                }
            if (this.selectedWord != null) {
                this.readingLabel.setText(this.selectedWord.kana());
                this.translationLabel.setText(this.selectedWord.translation());
                this.translationTooltip.setText(this.selectedWord.translation());
            }
        }
    }

    private void onEnterReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            onSearch();
        else {
            int caretPosition = this.searchTextField.getCaretPosition();
            int lenBefore = this.searchTextField.getText().length();
            this.searchTextField.setText(this.hiragana.transform(this.searchTextField.getText()));
            int lenAfter = this.searchTextField.getText().length();
            caretPosition = Math.max(Math.min(caretPosition + lenAfter - lenBefore, lenAfter), 0);
            this.searchTextField.positionCaret(caretPosition);
        }
    }

    private void onArrowsPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT)
            onPrevPage();
        else if (event.getCode() == KeyCode.RIGHT)
            onNextPage();
    }

    private void onSpinnerValueChanged(int value) {
        this.elemsPerPageSpinner.getValueFactory().setValue(this.paginator.setElemsPerPageNum(value));
        this.onSearch();
    }

    public static final int WIDTH = 800, HEIGHT = 600;
    public static final String TITLE = "三万語文集";

    private Scene scene;

    private TextField searchTextField;
    private Spinner<Integer> elemsPerPageSpinner;
    private Label readingLabel, translationLabel, paginatorLabel;
    private ObservableList<String> listItems;
    private ListView<String> listView;
    private Tooltip translationTooltip;
    private Paginator<Pair<Integer, Word>> paginator;
    private Word selectedWord;
    private Hiragana hiragana;
    private Trie<Word> trie;
}
