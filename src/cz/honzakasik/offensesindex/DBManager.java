package cz.honzakasik.offensesindex;

import com.healthmarketscience.sqlbuilder.*;
import com.healthmarketscience.sqlbuilder.dbspec.basic.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cz.honzakasik.offensesindex.DatabaseNames.*;
/**
 * Created by Jan Kasik on 28.10.15.
 */
public class DBManager {

    private Connection connection;
    private DbSpec spec = new DbSpec();
    private DbSchema schema = spec.addDefaultSchema();
    private DbTable offensesTable;
    private DbTable driversTable;
    private DbTable policemenTable;
    private DbTable eventsTable;
    private DbTable departmentsTable;
    private Logger logger = Logger.getLogger(DBManager.class.getName());

    public DBManager() {
        initializeTables();
        String url = "jdbc:derby:prestupky_db;create=true";
        try {
            this.connection = DriverManager.getConnection(url);
            if (!isTableReady(DRIVERS))
                createTable(driversTable);
            if (!isTableReady(EVENTS))
                createTable(eventsTable);
            if (!isTableReady(OFFENSES))
                createTable(offensesTable);
            if (!isTableReady(DEPARTMENTS))
                createTable(departmentsTable);
            if (!isTableReady(POLICEMEN))
                createTable(policemenTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getDrivers() {
        return executeSQL(getDriverQuery(BinaryCondition.EMPTY));
    }

    public ResultSet getDriversWhoLostLicenseFromCity(String city) {
        return executeSQL(getDriverQuery(
                ComboCondition.and(BinaryCondition.greaterThan(POINT_COUNT, 12, true),
                                    BinaryCondition.equalTo(driversTable.findColumn(CITY), city))));
    }

    public ResultSet getDriversFromTo(LocalDate[] dates) {
        return executeSQL(getDriverQuery(
                ComboCondition.and(BinaryCondition.greaterThan(eventsTable.findColumn(DATE), dates[0], true),
                                    BinaryCondition.lessThan(eventsTable.findColumn(DATE), dates[1], true))));
    }

    private ResultSet executeSQL(String SQL) {
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            logger.log(Level.INFO, "Executing: " + SQL);
            statement.execute(SQL);
            return statement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isResultEmpty(ResultSet result) {
        try {
            return !result.first();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private String getDriverQuery(Condition condition) {
        return new SelectQuery()
                .addCustomColumns(
                        FunctionCall.count().addColumnParams(eventsTable.findColumn(ID)),
                        FunctionCall.sum().addColumnParams(offensesTable.findColumn(POINT_COUNT)),
                        driversTable.findColumn(NAME),
                        driversTable.findColumn(SURNAME),
                        driversTable.findColumn(CITY),
                        driversTable.findColumn(ID))
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
                .addCondition(condition).toString();
    }

    private DbTable initializeDriversTable() {
        DbTable table = new DbTable(schema, DRIVERS);
        table.addColumn(ID, "BIGINT", null).notNull();
        table.addColumn(NAME, "VARCHAR", 50).notNull();
        table.addColumn(SURNAME, "VARCHAR", 50).notNull();
        table.addColumn(DATE_OF_BIRTH, "DATE", null).notNull();
        table.addColumn(SEX, "VARCHAR", 10).notNull();
        table.addColumn(CITY, "VARCHAR", 50).notNull();
        table.addColumn(POINT_COUNT, "SMALLINT", null).notNull();
        return table;
    }

    private DbTable initializeEventsTable() {
        DbTable table = new DbTable(schema, EVENTS);
        table.addColumn(ID, "BIGINT", null);
        table.addColumn(OFFENSE_ID, "BIGINT", null);
        table.addColumn(DRIVER_ID, "BIGINT", null);
        table.addColumn(POLICEMAN_ID, "BIGINT", null);
        table.addColumn(DATE, "DATE", null);
        table.addColumn(DESCRIPTION, "VARCHAR", 5000);
        return table;
    }

    private DbTable initializePolicemenTable() {
        DbTable table = new DbTable(schema, POLICEMEN);
        table.addColumn(ID, "BIGINT", null).notNull();
        table.addColumn(NAME, "VARCHAR", 50).notNull();
        table.addColumn(SURNAME, "VARCHAR", 50).notNull();
        table.addColumn(NUMBER, "BIGINT", null).notNull();
        table.addColumn(DEPARTMENT_ID, "BIGINT", null).notNull();
        return table;
    }

    private DbTable initializeDepartmentsTable() {
        DbTable table = new DbTable(schema, DEPARTMENTS);
        table.addColumn(ID, "BIGINT", null).notNull();
        table.addColumn(NAME, "VARCHAR", 50).notNull();
        table.addColumn(CITY, "VARCHAR", 50).notNull();
        return table;
    }

    private DbTable initializeOffensesTable() {
        DbTable table = new DbTable(schema, OFFENSES);
        table.addColumn(ID, "BIGINT", null).notNull();
        table.addColumn(POINT_COUNT, "SMALLINT", null).notNull();
        table.addColumn(NAME, "VARCHAR", 100).notNull();
        return table;
    }

    public ResultSet getAllCities() {
        return executeSQL("SELECT DISTINCT \"" + CITY + "\" FROM \"" + DRIVERS + "\"");
    }

    private void createTable(DbTable table) {
        String createTable = new CreateTableQuery(table, true).toString();
        executeSQL(createTable);
    }

    private boolean isTableReady(String table) {
        try {
            DatabaseMetaData dbm = connection.getMetaData();
            try (ResultSet tables = dbm.getTables(null, null, table.toUpperCase(), null)) {
                return tables.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ObservableList<DepartmentTableItem> getDepartmentsWithinYear(int year) {
        LocalDate start = LocalDate.ofYearDay(year, 1);
        LocalDate end = LocalDate.ofYearDay(year, start.isLeapYear() ? 366 : 365);
        String customQuery = new SelectQuery()
                .addCustomColumns(
                        FunctionCall.count().addColumnParams(eventsTable.findColumn(ID)),
                        departmentsTable.findColumn(CITY),
                        departmentsTable.findColumn(NAME),
                        departmentsTable.findColumn(ID))
                .addCustomJoin(SelectQuery.JoinType.INNER,
                        eventsTable, departmentsTable,
                        BinaryCondition.equalTo(departmentsTable.findColumn(ID), eventsTable.findColumn(DEPARTMENT_ID)))
                .addCustomGroupings
                        (departmentsTable.findColumn(CITY),
                        departmentsTable.findColumn(NAME),
                        departmentsTable.findColumn(ID))
                .addCondition(ComboCondition.and(BinaryCondition.greaterThan(eventsTable.findColumn(DATE), start, true),
                                                BinaryCondition.lessThan(eventsTable.findColumn(DATE), end, true)))
                .validate().toString();
        executeSQL(customQuery);
        return FXCollections.emptyObservableList();
    }

    private void initializeTables() {
        departmentsTable = initializeDepartmentsTable();
        offensesTable = initializeOffensesTable();
        policemenTable = initializePolicemenTable();
        driversTable = initializeDriversTable();
        eventsTable = initializeEventsTable();
    }
}
