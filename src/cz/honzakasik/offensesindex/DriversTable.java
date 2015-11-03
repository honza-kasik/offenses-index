package cz.honzakasik.offensesindex;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static cz.honzakasik.offensesindex.DatabaseNames.*;

/**
 * Created by Jan Kasik on 28.10.15.
 */
public class DriversTable {

    @FXML private TableView<DriverTableItem> driversTable;

    private DBManager DBManager = new DBManager();

    public void initialize() {
        ResultSet result = DBManager.getDrivers();
        driversTable.setItems(createTableData(result));
    }

    public void initialize(String city) {
        fillTableWithDriversFromCity(city);
    }

    private void fillTableWithDriversFromCity(String city) {
        ResultSet result = DBManager.getDriversWhoLostLicenseFromCity(city);
        driversTable.setItems(createTableData(result));
    }

    public void initialize(LocalDate[] dates) {
        fillTableWithFromToDatesResult(dates);
    }

    private void fillTableWithFromToDatesResult(LocalDate[] dates) {
        ResultSet result = DBManager.getDriversFromTo(dates);
        driversTable.setItems(createTableData(result));
    }

    private ObservableList<DriverTableItem> createTableData(ResultSet result) {
        ObservableList<DriverTableItem> data = FXCollections.observableArrayList();
        try {
            while (result != null && result.next()) {
                 DriverTableItem driverTableItem = new DriverTableItem(
                        result.getString(NAME),
                        result.getString(SURNAME),
                        result.getInt(POINT_COUNT),
                        result.getInt(OFFENSES_COUNT),
                        result.getString(CITY));
                data.add(driverTableItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

}
