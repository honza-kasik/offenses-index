package cz.honzakasik.offensesindex.database;

import com.healthmarketscience.sqlbuilder.*;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import cz.honzakasik.offensesindex.drivers.Driver;
import cz.honzakasik.offensesindex.drivers.DriverTableItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static cz.honzakasik.offensesindex.database.DatabaseNames.*;

/**
 * Created by Jan Kasik on 14.11.15.
 */
public class DriversDBManager {

    private DBManager dbManager;

    private DbTable driversTable;
    private DbTable offensesTable;
    private DbTable eventsTable;

    public DriversDBManager(DBManager dbManager) {
        this.dbManager = dbManager;
        driversTable = dbManager.getDriversTable();
        offensesTable = dbManager.getOffensesTable();
        eventsTable = dbManager.getEventsTable();
    }

    public ObservableList<DriverTableItem> getDrivers() {
        return getDrivers(BinaryCondition.EMPTY, BinaryCondition.EMPTY);
    }

    public ObservableList<DriverTableItem> getDriversWhoLostLicenseFromCity(String city) {
        return getDrivers(
                BinaryCondition.greaterThan(
                        FunctionCall.sum().addColumnParams(offensesTable.findColumn(POINT_COUNT)),
                        12, true),
                BinaryCondition.equalTo(driversTable.findColumn(CITY), city));
    }

    public ObservableList<DriverTableItem> getDriversFromTo(LocalDate[] dates) {
        return getDrivers(
                BinaryCondition.EMPTY,
                ComboCondition.and(
                        BinaryCondition.greaterThan(eventsTable.findColumn(DATE), dates[0], true),
                        BinaryCondition.lessThan(eventsTable.findColumn(DATE), dates[1], true)));
    }

    private ObservableList<DriverTableItem> getDrivers(Condition havingCondition, Condition condition) {
        String query = new SelectQuery()
                .addCustomColumns(
                        driversTable.getColumns().toArray())
                .addAliasedColumn(
                        FunctionCall.sum().addColumnParams(offensesTable.findColumn(POINT_COUNT)),
                        POINT_COUNT)
                .addAliasedColumn(
                        FunctionCall.count().addColumnParams(offensesTable.findColumn(ID)),
                        OFFENSES_COUNT)
                .addJoin(SelectQuery.JoinType.RIGHT_OUTER,
                        eventsTable,
                        driversTable,
                        BinaryCondition.equalTo(eventsTable.findColumn(DRIVER_ID), driversTable.findColumn(ID)))
                .addJoin(SelectQuery.JoinType.LEFT_OUTER,
                        eventsTable,
                        offensesTable,
                        BinaryCondition.equalTo(eventsTable.findColumn(OFFENSE_ID), offensesTable.findColumn(ID)))
                .addCustomGroupings(driversTable.getColumns().toArray())
                .addHaving(havingCondition)
                .addCondition(condition)
                .validate().toString();
        return transformDriverTableData(dbManager.executeSQL(query));
    }

    public void insertNewDriver(Driver driver) {
        String query = new InsertQuery(driversTable)
                .addColumn(driversTable.findColumn(NAME), driver.getName())
                .addColumn(driversTable.findColumn(SURNAME), driver.getSurname())
                .addColumn(driversTable.findColumn(CITY), driver.getCity())
                .addColumn(driversTable.findColumn(STREET), driver.getStreet())
                .addColumn(driversTable.findColumn(ID), driver.getId())
                .addColumn(driversTable.findColumn(GENDER), driver.getGender())
                .addColumn(driversTable.findColumn(DATE_OF_BIRTH), driver.getDateOfBirth())
                .validate().toString();
        dbManager.executeSQL(query);
    }

    public ObservableList<DriverTableItem> transformDriverTableData(ResultSet result) {
        ObservableList<DriverTableItem> data = FXCollections.observableArrayList();
        try {
            while (result != null && result.next()) {
                DriverTableItem driverTableItem = new DriverTableItem(
                        result.getString(NAME),
                        result.getString(SURNAME),
                        result.getString(CITY),
                        result.getString(STREET),
                        result.getInt(ID),
                        result.getString(GENDER),
                        result.getDate(DATE_OF_BIRTH).toLocalDate(),
                        result.getInt(POINT_COUNT),
                        result.getInt(OFFENSES_COUNT));
                data.add(driverTableItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

}
