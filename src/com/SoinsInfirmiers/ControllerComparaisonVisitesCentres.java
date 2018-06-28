package SoinsInfirmiers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import main.ExceptionHandler;
import main.Menu;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ControllerComparaisonVisitesCentres implements Initializable {

    @FXML
    private TableView<ObservableList> tableView;
    @FXML
    private VBox menuBox;
    @FXML
    private BarChart barChart;
    @FXML
    private TextField yearPicker1;
    @FXML
    private GridPane waitingPane;

    final private Menu menu = new Menu();
    final private Database database = new Database();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menu.loadMenuBar(menuBox);
    }

    public void onAction() {
        waitingPane.setVisible(true);
        tableView.getColumns().clear();
        barChart.getData().clear();
        new Thread(() -> {
            startQuery();
        }).start();
    }

    private void startQuery() {
        int rowCount = 1;
        Platform.runLater(() -> tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY));
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String fromYear = yearPicker1.getText();
        String toYear = String.valueOf(Integer.parseInt(fromYear)+1);
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        XYChart.Series series = new XYChart.Series();
        try {
            conn = database.connect();
            String query = database.setQuery(Database.Query.VISITES_PAR_CENTRE);
            ps = conn.prepareStatement(query);
            ps.setDate(1, java.sql.Date.valueOf(""+ fromYear + "-01-01"));
            ps.setDate(2, java.sql.Date.valueOf(""+ toYear + "-01-01"));
            rs = ps.executeQuery();

            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                final int j = i - 1;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i));
                col.setCellFactory(TextFieldTableCell.forTableColumn());
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
                Platform.runLater(() -> tableView.getColumns().addAll(col));
                System.out.println("Column [" + i + "] ");
            }
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                System.out.println("Row [" + rowCount + "]" + row);
                rowCount++;
                data.add(row);
                // Starting rowCount @ 2 to avoid having the 'ALL' column in the chart
                if (rowCount > 2 ) {
                    series.getData().add(new XYChart.Data<>(rs.getString(1), rs.getDouble(14)));
                }
            }
            tableView.setItems(data);
            Platform.runLater(() -> generateBarChart(series));
        } catch (Exception e) {
            ExceptionHandler.switchException(e, this.getClass());
        } finally {
            waitingPane.setVisible(false);
            database.close(rs);
            database.close(ps);
            database.close(conn);
        }
    }

    private void generateBarChart(XYChart.Series series){
        barChart.getData().add(series);
    }
}
