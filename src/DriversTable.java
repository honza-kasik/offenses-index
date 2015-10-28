import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.sql.*;
import java.time.LocalDate;

/**
 * Created by Jan Kasik on 28.10.15.
 */
public class DriversTable {

    @FXML private TableView<DriverTableItem> driversTable;

    private Dispatcher dispatcher = new Dispatcher();

    public void initialize() {
        ResultSet result = dispatcher.getDrivers();
        driversTable.setItems(fillTableData(result));
    }

    public void initialize(String city) {
        fillTableWithDriversFromCity(city);
    }

    private void fillTableWithDriversFromCity(String city) {
        ResultSet result = dispatcher.getDriversFromCity(city);
        driversTable.setItems(fillTableData(result));
    }

    public void initialize(LocalDate[] dates) {
        fillTableWithFromToDatesResult(dates);
    }

    private void fillTableWithFromToDatesResult(LocalDate[] dates) {
        ResultSet result = dispatcher.getDriversFromTo(dates);
        driversTable.setItems(fillTableData(result));
    }

    private ObservableList<DriverTableItem> fillTableData(ResultSet result) {
        ObservableList<DriverTableItem> data = FXCollections.emptyObservableList();
        try {
            while (result != null && result.next()) {
                data.add(new DriverTableItem(
                        result.getString("jmeno"),
                        result.getString("prijmeni"),
                        result.getInt("pocet_bodu"),
                        result.getInt("pocet_prestupku"),
                        result.getString("mesto")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

}
