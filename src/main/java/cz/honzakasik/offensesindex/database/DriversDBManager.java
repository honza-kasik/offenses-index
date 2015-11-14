package cz.honzakasik.offensesindex.database;

import com.healthmarketscience.sqlbuilder.*;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import java.sql.ResultSet;
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

    public ResultSet getDrivers() {
        return getDrivers(BinaryCondition.EMPTY, BinaryCondition.EMPTY);
    }

    public ResultSet getDriversWhoLostLicenseFromCity(String city) {
        return getDrivers(
                BinaryCondition.greaterThan(
                        FunctionCall.sum().addColumnParams(offensesTable.findColumn(POINT_COUNT)),
                        12, true),
                BinaryCondition.equalTo(driversTable.findColumn(CITY), city));
    }

    public ResultSet getDriversFromTo(LocalDate[] dates) {
        return getDrivers(
                BinaryCondition.EMPTY,
                ComboCondition.and(
                        BinaryCondition.greaterThan(eventsTable.findColumn(DATE), dates[0], true),
                        BinaryCondition.lessThan(eventsTable.findColumn(DATE), dates[1], true)));
    }

    private ResultSet getDrivers(Condition havingCondition, Condition condition) {
        String query = new SelectQuery()
                .addCustomColumns(
                        driversTable.findColumn(NAME),
                        driversTable.findColumn(SURNAME),
                        driversTable.findColumn(CITY),
                        driversTable.findColumn(ID))
                .addAliasedColumn(
                        FunctionCall.sum().addColumnParams(offensesTable.findColumn(POINT_COUNT)),
                        POINT_COUNT)
                .addAliasedColumn(
                        FunctionCall.count().addColumnParams(offensesTable.findColumn(ID)),
                        OFFENSES_COUNT)
                .addJoin(SelectQuery.JoinType.INNER,
                        eventsTable,
                        driversTable,
                        BinaryCondition.equalTo(eventsTable.findColumn(DRIVER_ID), driversTable.findColumn(ID)))
                .addJoin(SelectQuery.JoinType.INNER,
                        eventsTable,
                        offensesTable,
                        BinaryCondition.equalTo(eventsTable.findColumn(OFFENSE_ID), offensesTable.findColumn(ID)))
                .addGroupings(
                        driversTable.findColumn(ID),
                        driversTable.findColumn(NAME),
                        driversTable.findColumn(SURNAME),
                        driversTable.findColumn(CITY))
                .addHaving(havingCondition)
                .addCondition(condition)
                .validate().toString();
        return dbManager.executeSQL(query);
    }


    public DBManager getDbManager() {
        return dbManager;
    }
}
