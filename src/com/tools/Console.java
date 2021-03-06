package tools;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Console {

    public static Stage console = new Stage();
    private static TextArea text = new TextArea();

    static public void buildConsole() {
        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane);
        stackPane.getChildren().add(text);
        String style = "-fx-control-inner-background:#000;" +
                " -fx-highlight-fill: #EEE;" +
                " -fx-highlight-text-fill: #000; " +
                " -fx-text-fill: #FFF; ";
        text.setStyle(style);
        text.setEditable(false);

        String title = "CONSOLE";
        console.setTitle(title);
        console.setX(100);
        console.setY(120);
        console.setHeight(400);
        console.setWidth(600);
        console.setScene(scene);
        console.show();
    }

    static public void append(String string) {
        text.appendText(Time.getCurrentTime() + " - " + string);
    }

    static public void appendln(String string) {
        text.appendText(Time.getCurrentTime() + " - " + string + "\n");
    }

    static public void appendln(int number) {
        text.appendText(Time.getCurrentTime() + " - " + number + "\n");
    }

    static public void appendln(double number) {
        text.appendText(Time.getCurrentTime() + " - " + number + "\n");
    }
}
