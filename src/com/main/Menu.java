package main;

import SoinsInfirmiers.Data;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Menu {

    public static Stage pdfStage = new Stage();

    public void loadMenuBar(Pane pane) {
        try {
            pane.getChildren().add(FXMLLoader.load((getClass().getResource("/ui/FXML/Menu.fxml"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openHomepage() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/HomePage.fxml"));
            changeScene(root);
            stage.setTitle(AVJ.Data.homePageTitle);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void showPatchnote(){
        PatchNote pn = new PatchNote();
        pn.patchNote();
    }

    public void changeLogs(){
        String changelog = "/txt/Changelog.txt";
        File file = new File(changelog);
        if (!Desktop.isDesktopSupported()) {
            System.out.println("OS non supporté");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        try {
            if (file.exists()) desktop.open(file);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void print() {
        // TODO
    }

    public void openIndicateursPage(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/StatistiquesSI.fxml"));
            changeScene(root);
            stage.setTitle(Data.pageTitle0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openComparaisonPage(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/TableauxAnnuels.fxml"));
            changeScene(root);
            stage.setTitle(Data.pageTitle1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void pdfActivite(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/PopUpActivite.fxml"));
            pdfStage.setScene(new Scene(root));
            pdfStage.setTitle("Rapport d'activité et suivi du personnel");
            pdfStage.setResizable(false);
            pdfStage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openContingentPage(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/Contingent.fxml"));
            changeScene(root);
            stage.setTitle(AVJ.Data.pageTitle0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openASDB(){
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/ASDB.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(AVJ.Data.asdbTitle);
            stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void changeScene(Parent root){
        Stage stage = Main.getPrimaryStage();
        Scene scene = stage.getScene();
        scene.setRoot(root);
    }

    public void closeButtonAction() throws InterruptedException {
        System.exit(0);
    }

}
