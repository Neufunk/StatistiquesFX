package main;

import javafx.scene.image.Image;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;
import tools.Changelog;
import tools.Console;
import tools.Version;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class Menu {

    @FXML
    MenuBar menuBar;

    private static final Stage gestionStage = new Stage();
    static final Stage connectionTestStage = new Stage();

    static final String HOMEPAGE = "Aide & Soins à Domicile - STATISTIQUES - v" + Version.versionNumber;
    private final String TITLE_1 = "Soins Infirmiers - Comparaison par années - v" + Version.versionNumber;
    private final String TITLE_2 = "Soins Infirmiers - Répartition annuelle - v" + Version.versionNumber;
    static final String TITLE_3 = "Soins Infirmiers - Sélection - v" + Version.versionNumber;
    private final String TITLE_4 = "Soins Infirmiers - Comparaison par centres - v" + Version.versionNumber;
    private final String TITLE_5 = "Soins Infirmiers - Comparaison - visites par localités - v" + Version.versionNumber;
    private final String TITLE_6 = "Soins Infirmiers - Comparaison - visites par centres - v" + Version.versionNumber;
    private final String TITLE_7 = "Soins Infirmiers - Répartition par âge - v" + Version.versionNumber;

    public void openHomepage() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/HomePage.fxml"));
            changeScene(root);
            stage.setTitle(HOMEPAGE);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openSettings() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/Settings.fxml"));
            changeScene(root);
            stage.setTitle("Paramètres - " + Version.versionNumber);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void changeLog() {
        InputStream inputStream = this.getClass().getResourceAsStream("/txt/Changelog.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String str;
        try {
            while ((str = bufferedReader.readLine()) != null) {
                System.out.println(str);
                Changelog.append(str);
                Changelog.buildChangelog();
            }
        } catch (Exception e) {
                e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void print() {

    }

    public void openIndicateursPage() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/RepartitionAnnuelle.fxml"));
            changeScene(root);
            stage.setTitle(TITLE_2);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openComparaisonAnnees() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/ComparaisonAnnees.fxml"));
            changeScene(root);
            stage.setTitle(TITLE_1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openComparaisonCentres() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/ComparaisonCentres.fxml"));
            changeScene(root);
            stage.setTitle(TITLE_4);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openSelectVisitesPatients() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/SelectVisitesPatients.fxml"));
            Scene scene = stage.getScene();
            scene.setRoot(root);
            stage.setTitle(TITLE_3);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openVisitesCentres() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/CompaisonVisitesCentres.fxml"));
            changeScene(root);
            stage.setTitle(TITLE_6);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openVisitesLocalites() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/CompaisonVisitesLocalites.fxml"));
            changeScene(root);
            stage.setTitle(TITLE_5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openRepartitionAge() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/RepartitionAge.fxml"));
            changeScene(root);
            stage.setTitle(TITLE_7);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pdfGestion() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/PopUpGestion.fxml"));
            gestionStage.setScene(new Scene(root, 370, 420));
            gestionStage.setTitle("Rapport - Indicateurs de gestion");
            gestionStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/pdf-symbol-small.png")));
            gestionStage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openContingentPage() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/Contingent.fxml"));
            changeScene(root);
            stage.setTitle(avj.Data.pageTitle0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openASDB() {
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/ASDB.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(avj.Data.asdbTitle);
            stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openConnectionTest() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/ConnectionTest.fxml"));
            connectionTestStage.setResizable(false);
            connectionTestStage.setScene(new Scene(root, 280, 350));
            connectionTestStage.setTitle("Tester les connexions");
            connectionTestStage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void changeScene(Parent root) {
        Stage stage = Main.getPrimaryStage();
        Scene scene = stage.getScene();
        scene.setRoot(root);
    }

    public void openSelectSi() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/SelectSi.fxml"));
            Scene scene = stage.getScene();
            scene.setRoot(root);
            stage.setTitle(TITLE_3);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openSelectAvj() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/SelectAVJ.fxml"));
            Scene scene = stage.getScene();
            scene.setRoot(root);
            stage.setTitle(avj.Data.pageTitle1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openLog() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/Log.fxml"));
            connectionTestStage.setResizable(true);
            connectionTestStage.setScene(new Scene(root, 1200, 980));
            connectionTestStage.setTitle("LOG");
            connectionTestStage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void detectJavaVersion() {
        final String JAVA_VERSION = System.getProperty("java.version");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Version de JAVA installée sur le système");
        alert.setHeaderText("La version de JAVA installée sur votre système et actuellement utilisée par STATISTIQUES :");
        alert.setContentText("JRE " + JAVA_VERSION);
        alert.show();
        Console.appendln("\nJAVA VERSION DETECTED: " + JAVA_VERSION + "\n");
    }

    public void openAboutWindow() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        alert.setTitle("Logiciel Statistiques - AIDE & SOINS À DOMICILE");
        alert.setHeaderText("Logiciel Statistiques [version " + Version.versionNumber + "]");
        alert.setContentText("2017-" + year + "\nAIDE & SOINS À DOMICILE EN PROVINCE DE NAMUR\n" +
                "Code & Design JOHNATHAN VANBENEDEN \n" +
                "SDK 1.8.0_211 - Supported JRE : 8u172/181/201/211");
        alert.show();
    }

    public void openConsole() {
        Console.buildConsole();
    }

    public void closeButtonAction() {
        System.exit(0);
    }
}
