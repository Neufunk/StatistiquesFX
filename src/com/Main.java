package com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;

    private void setPrimaryStage(Stage stage) {
        Main.primaryStage = stage;
    }

    static public Stage getPrimaryStage() {
        return Main.primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        setPrimaryStage(primaryStage);
<<<<<<< HEAD
        Parent root = FXMLLoader.load(getClass().getResource("fxml/HomePage.fxml"));
        primaryStage.setTitle("Aide & Soins à Domicile - Statistiques // FX_Alpha 1");
=======
        Parent root = FXMLLoader.load(getClass().getResource("FXML/HomePage.fxml"));
        primaryStage.setTitle(Strings.homePageTitle);
>>>>>>> a33b3a7b1954e1a86b3af28986c5591d0a2475e8
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
