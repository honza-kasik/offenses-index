package cz.honzakasik.offensesindex;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.time.LocalDate;

/**
 * Created by Jan Kasik on 28.10.15.
 */
public class DriversTable {

    @FXML private TableView<DriverTableItem> driversTable;

    private DBManager DBManager = new DBManager();

    public void initialize() {
        ResultSet result = DBManager.getDrivers();
        driversTable.setItems(Helper.transformDriverTableData(result));
    }

    public void initialize(String city) {
        fillTableWithDriversFromCity(city);
    }

    private void fillTableWithDriversFromCity(String city) {
        ResultSet result = DBManager.getDriversWhoLostLicenseFromCity(city);
        driversTable.setItems(Helper.transformDriverTableData(result));
    }

    public void initialize(LocalDate[] dates) {
        fillTableWithFromToDatesResult(dates);
    }

    private void fillTableWithFromToDatesResult(LocalDate[] dates) {
        ResultSet result = DBManager.getDriversFromTo(dates);
        driversTable.setItems(Helper.transformDriverTableData(result));
    }



}
