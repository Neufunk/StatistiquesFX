package main;

import SoinsInfirmiers.Data;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePage implements Initializable {

    public JFXButton closeButton;
    @FXML
    private JFXButton siButton;
    @FXML
    private JFXButton avjButton;
    @FXML
    private VBox menuPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onButtonSiClick();
        onButtonAvjClick();
        Menu menu = new Menu();
        menu.loadMenuBar(menuPane);
    }

    private void onButtonSiClick(){
        Tooltip.install(siButton, new Tooltip("Département Soins Infirmiers"));
        siButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> openIndicateursPage());
    }

    private void onButtonAvjClick(){
        avjButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> openContingentPage());
    }

    private void openIndicateursPage(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/StatistiquesSI.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(Data.pageTitle0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void openContingentPage(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/Contingent.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(AVJ.Data.pageTitle0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void closeButtonAction() throws InterruptedException {
        System.exit(0);
    }

}

