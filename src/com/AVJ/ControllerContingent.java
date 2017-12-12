package AVJ;

import RingProgressIndicator.RingProgressIndicator;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import main.Effects;
import main.Main;
import main.Menu;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ResourceBundle;

import static AVJ.IteratorExcel.getNonUpdated;

public class ControllerContingent implements Initializable {

    @FXML
    private JFXComboBox comboCentre;
    @FXML
    private JFXComboBox comboSecteur;
    @FXML
    private JFXComboBox comboPeriode;
    @FXML
    private AnchorPane mainPane;
    @FXML
    public AnchorPane maskPane;
    @FXML
    private TableView<ObservableList> tableView;
    @FXML
    private TableView<ObservableList> tableView2;
    @FXML
    private JFXToggleButton toggleButton;
    @FXML
    private JFXToggleButton toggleButton1;
    @FXML
    private Label yearLabel1;
    @FXML
    private Label yearLabel2;
    @FXML
    private JFXCheckBox antenneCheckbox;
    @FXML
    private VBox menuPane;

    private Database database = new Database();
    private Effects fx = new Effects();
    private static Stage progressStage = new Stage();
    private RingProgressIndicator ringProgressIndicator = new RingProgressIndicator();
    private volatile boolean runningThreadFlag = true;
    static int progress = 0;
    String secteurLabel = "Initialisation...";
    static TextField textField = new TextField();
    private static TextArea shellTextArea = new TextArea();
    private Stage eventShell = new Stage();
    private Button cancelButton = new Button("Annuler");

    public void initialize(URL location, ResourceBundle resources) {
        Menu menu = new Menu();
        menu.loadMenuBar(menuPane);
        initializeCombo();
        onCancelButtonClick();
        toggleButtonListener();
        toggleButtonDirectriceListener();
        if (progressStage.getOwner() != Main.getPrimaryStage()) {
            progressWindowProperties();
        }
    }

    private void initializeCombo() {
        AVJ.Data data = new AVJ.Data();
        comboCentre.setItems(data.centerList);
        comboPeriode.setItems(data.periode);
    }

    public void toggleButtonListener() {
        toggleButton.setText(getCurrentYear());
    }

    public void toggleButtonDirectriceListener() {
        if (toggleButton1.isSelected()) {
            toggleButton1.setText("Laurence");
        } else {
            toggleButton1.setText("Sarah");
        }
    }

    public void displaySecteurs() {
        if (comboCentre.getValue() == "ASD") {
            comboSecteur.setPromptText("Province entière");
            comboSecteur.setDisable(true);
            antenneCheckbox.setDisable(true);
            antenneCheckbox.setSelected(false);
        } else if (antenneCheckbox.isSelected()) {
            comboSecteur.setDisable(true);
            comboSecteur.getSelectionModel().clearSelection();
            comboSecteur.setPromptText("Secteur entier");
        } else if (!antenneCheckbox.isSelected() && comboCentre.getValue() == null) {
            comboSecteur.setDisable(true);
            comboSecteur.setPromptText("Selectionnez un centre");
        } else {
            comboSecteur.setPromptText("Secteurs");
            comboSecteur.setDisable(false);
            antenneCheckbox.setDisable(false);
        }
        database.connect();
        if (comboCentre.getValue() != null) {
            comboSecteur.setItems(database.loadSectorsToCombo(comboCentre.getValue().toString()));
        }
        database.closeConnection();
    }

    private boolean getCheckboxState() {
        return antenneCheckbox.isSelected();
    }

    public void onUpdateButtonClick() {
        buildProgressWindow();
        new WorkerThread(ringProgressIndicator).start();
        startUpdate();
    }

    private void startUpdate() {
        runningThreadFlag = true;
        IteratorExcel iteratorExcel = new IteratorExcel();
        final Service<Void> calculateService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        String sql = "SELECT * FROM travailleurs " +
                                "INNER JOIN secteurs " +
                                "ON travailleurs.id = secteurs.worker_id ";
                        Connection connection = database.connect();
                        try {
                            Statement statement = connection.createStatement();
                            ResultSet rs = statement.executeQuery(sql);
                            while (rs.next()) {
                                if (!runningThreadFlag) {
                                    break;
                                } else {
                                    progress += 1;
                                    String name = rs.getString("prenom");
                                    String sect = rs.getString("secteur_name");
                                    String centre = rs.getString("antenne");
                                    String namPath;
                                    String philPath;
                                    if (toggleButton1.isSelected()) {
                                        namPath = "P:\\SERVICE SOCIAL - SERVICE DU PERSONNEL\\Tableaux mensuels\\";
                                        philPath = "W:\\SERVICE FAMILIAL\\SERVICE SOCIAL - SERVICE DU PERSONNEL\\Tableaux mensuels\\";
                                    } else {
                                        namPath = "W:\\SERVICE SOCIAL - SERVICE DU PERSONNEL\\Tableaux mensuels\\";
                                        philPath = "P:\\SERVICE FAMILIAL\\SERVICE SOCIAL - SERVICE DU PERSONNEL\\Tableaux mensuels\\";
                                    }
                                    switch (centre) {
                                        case "Namur":
                                            iteratorExcel.startIteration(namPath, getCurrentYear(), name, sect, connection);
                                            progress += 1;
                                            break;
                                        case "Philippeville":
                                            iteratorExcel.startIteration(philPath, getCurrentYear(), name, sect, connection);
                                            progress += 1;
                                            break;
                                    }
                                }
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                            shellTextArea.appendText(e1.getMessage());
                            displayError(e1);
                        }
                        return null;
                    }
                };
            }
        };
        calculateService.stateProperty().addListener((ObservableValue<? extends Worker.State> observableValue, Worker.State oldValue, Worker.State newValue) -> {
            switch (newValue) {
                case FAILED:
                    closeProgressWindow();
                    database.closeConnection();
                    eventShell.setTitle("EventShell - Failed");
                    shellTextArea.appendText("FAILED");
                    System.out.println("FAILED");
                    break;
                case CANCELLED:
                    closeProgressWindow();
                    database.closeConnection();
                    eventShell.setTitle("EventShell - Cancelled");
                    shellTextArea.appendText("CANCELLED");
                    System.out.println("CANCELLED");
                    break;
                case SUCCEEDED:
                    closeProgressWindow();
                    database.closeConnection();
                    eventShell.setTitle("EventShell - Updated");
                    updateFinishedAlert();
                    shellTextArea.appendText("SUCCEEDED");
                    System.out.println("SUCCEEDED");
                    break;
            }
        });
        calculateService.start();
    }

    private void updateFinishedAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mise à jour terminée");
        alert.setHeaderText("La mise à jour de la base de données s'est terminée sans erreur. \n Si des secteurs n'ont pas pu être mis à jour, ils apparaitront plus bas");
        alert.setContentText(getNonUpdated());
        alert.show();
    }

    private String getCurrentYear() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        if (toggleButton.isSelected()) {
            return String.valueOf(year);
        } else {
            return String.valueOf(year - 1);
        }
    }

    private void displayYearLabel() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        yearLabel1.setVisible(true);
        yearLabel2.setVisible(true);
        yearLabel1.setText(String.valueOf(year - 1));
        yearLabel2.setText(String.valueOf(year - 2));
        if (toggleButton.isSelected()) {
            yearLabel1.setText(String.valueOf(year));
            yearLabel2.setText(String.valueOf(year - 1));
        }
    }

    public void onGenerateButtonClick() {
        if (checkEmpty()) {
            displayTable();
        }
    }

    private void displayTable() {
        tableView.getColumns().clear();
        tableView2.getColumns().clear();
        displayYearLabel();
        ObservableList<ObservableList> observableList = FXCollections.observableArrayList();
        ObservableList<ObservableList> observableList2 = FXCollections.observableArrayList();
        try {
            Connection c = database.connect();
            String centre = comboCentre.getValue().toString();
            String secteur = "";
            if (comboSecteur.getValue() != null) {
                secteur = comboSecteur.getValue().toString();
            }
            String periode = comboPeriode.getValue().toString();
            String year = getCurrentYear();
            // CURRENT YEAR TABLE
            String sql = database.loadContingent38(centre, secteur, periode, year, getCheckboxState());
            ResultSet rs = c.createStatement().executeQuery(sql);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellFactory(TextFieldTableCell.forTableColumn());
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
                tableView.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] ajoutée " + row);
                observableList.add(row);
            }
            // LAST YEAR TABLE
            Calendar now = Calendar.getInstance();
            int currentYear = now.get(Calendar.YEAR);
            String lastYear = String.valueOf(currentYear - 1);
            if (toggleButton.isSelected()) {
                lastYear = String.valueOf(currentYear - 2);
            }
            String sql2 = database.loadContingent38(centre, secteur, periode, lastYear, getCheckboxState());
            ResultSet rs2 = c.createStatement().executeQuery(sql2);

            for (int i = 0; i < rs2.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs2.getMetaData().getColumnName(i + 1));
                col.setCellFactory(TextFieldTableCell.forTableColumn());
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
                tableView2.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }
            while (rs2.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs2.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs2.getString(i));
                }
                System.out.println("Row [1] ajoutée " + row);
                observableList2.add(row);
            }
            tableView.setItems(observableList);
            tableView2.setItems(observableList2);
            database.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la construction des données");
            displayError(e);
        }
    }

    private boolean checkEmpty() {
        if (comboCentre.getValue() == null) {
            showEmptyDialog("un centre");
            return false;
        } else if (!antenneCheckbox.isSelected() && comboSecteur.getValue() == null && comboCentre.getValue() != "ASD") {
            showEmptyDialog("un secteur");
            return false;
        } else if (comboPeriode.getValue() == null) {
            showEmptyDialog("une période");
            return false;
        } else {
            return true;
        }
    }

    private void showEmptyDialog(String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("NullPointerException");
        alert.setHeaderText("Veuillez sélectionner " + string);
        alert.setContentText("avoid NullPointerException");
        alert.showAndWait();
    }

    private void displayError(Exception e) {
        e.printStackTrace();
        String e1 = e.toString();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(e1);
        alert.setContentText("STACKTRACE : \t\t" + Arrays.toString(e.getStackTrace()) + "\n" +
                "CAUSE : \t\t\t" + e.getLocalizedMessage() + "\n" + "\t\t" + this.getClass().toString());
        alert.showAndWait();
    }

    /* Progress bar */
    private void progressWindowProperties() {
        progressStage.initOwner(Main.getPrimaryStage());
        progressStage.initModality(Modality.APPLICATION_MODAL);
        progressStage.initStyle(StageStyle.UNDECORATED);
        progressStage.initStyle(StageStyle.TRANSPARENT);
        progressStage.setOpacity(0.95);
        progressStage.setWidth(380);
        progressStage.setHeight(300);
        progressStage.setX(Main.getPrimaryStage().getX() + Main.getPrimaryStage().getWidth() / 2 - progressStage.getWidth() / 2);
        progressStage.setY(Main.getPrimaryStage().getY() + Main.getPrimaryStage().getHeight() / 2 - progressStage.getHeight() / 2);
    }

    private void buildProgressWindow() {
        progress = 0;
        StackPane stackPane = new StackPane();
        HBox hBoxTop = new HBox(10);
        HBox hBoxBottom = new HBox(10);
        Scene scene = new Scene(stackPane);

        scene.setFill(Color.TRANSPARENT);

        ringProgressIndicator.setRingWidth(400);
        ringProgressIndicator.makeIndeterminate();

        stackPane.setPadding(new Insets(0, 0, 30, 0));
        stackPane.setStyle("" +
                "-fx-background-radius: 20; " +
                "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,1) , 5, 0.0 , 0 , 1 ); " +
                "-fx-background-insets: 12;");
        stackPane.getChildren().add(ringProgressIndicator);
        stackPane.getChildren().add(hBoxTop);
        stackPane.getChildren().add(hBoxBottom);

        hBoxTop.setAlignment(Pos.TOP_CENTER);
        textField.setText(secteurLabel);
        textField.setMinWidth(150);
        hBoxTop.getChildren().add(textField);
        hBoxBottom.setAlignment(Pos.BOTTOM_CENTER);
        hBoxBottom.getChildren().add(cancelButton);

        maskPane.setVisible(true);
        fx.setFadeTransition(maskPane, 600, 0, 0.5);
        fx.setBoxBlur(mainPane);

        progressStage.setScene(scene);
        progressStage.show();
        buildEventShell();
    }

    private void buildEventShell() {
        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane);

        stackPane.getChildren().add(shellTextArea);
        shellTextArea.clear();
        shellTextArea.setStyle("-fx-control-inner-background:#000000; -fx-highlight-fill: #00ff00; " +
                "-fx-highlight-text-fill: #000000; -fx-text-fill: #ffffff; ");
        shellTextArea.setEditable(false);

        eventShell.setTitle("EventShell - Updating...");
        eventShell.setX(Main.getPrimaryStage().getX() + Main.getPrimaryStage().getWidth() / 1 - eventShell.getWidth() / 2);
        eventShell.setY(Main.getPrimaryStage().getY() + Main.getPrimaryStage().getHeight() / 1 - eventShell.getHeight() / 2);
        eventShell.setScene(scene);
        eventShell.setHeight(300);
        eventShell.setWidth(500);
        eventShell.show();
    }

    private void onCancelButtonClick() {
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> runningThreadFlag = false);
    }

    private void closeProgressWindow() {
        fx.unsetBoxBlur(mainPane);
        maskPane.setVisible(false);
        progressStage.close();
    }

    class WorkerThread extends Thread {
        RingProgressIndicator rpi;

        // int progress = 0;
        WorkerThread(RingProgressIndicator rpi) {
            this.rpi = rpi;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() ->
                        rpi.setProgress(progress));
                if (progress > 100) {
                    Platform.runLater(() ->
                            ringProgressIndicator.makeIndeterminate());
                    break;
                }
            }
        }

    }

    void addShell(String commandLine) {
        shellTextArea.appendText(commandLine + "\n");
    }

}

