package cz.honzakasik.offensesindex.database;

import com.healthmarketscience.sqlbuilder.*;
import com.healthmarketscience.sqlbuilder.dbspec.basic.*;
import cz.honzakasik.offensesindex.database.populator.DBPopulator;

import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cz.honzakasik.offensesindex.database.DatabaseNames.*;

/**
 * Created by Jan Kasik on 28.10.15.
 */
public class DBManager {

    private Connection connection;
    private DbSpec spec = new DbSpec();
    private DbSchema schema = spec.addDefaultSchema();
    private DbTable offensesTable = initializeOffensesTable();
    private DbTable driversTable = initializeDriversTable();
    private DbTable policemenTable = initializePolicemenTable();
    private DbTable eventsTable = initializeEventsTable();
    private DbTable departmentsTable = initializeDepartmentsTable();
    private Logger logger = Logger.getLogger(DBManager.class.getName());
    /**
     * dpPopulated is intended to use as flag only. If any new table is created, flag is set to false.
     */
    private boolean dbPopulated = true;

    public boolean isDbPopulated() {
        return dbPopulated;
    }

    public void setDbPopulated(boolean dbPopulated) {
        this.dbPopulated = dbPopulated;
    }

    public DBManager() {
        //initializeTables();
        String url = "jdbc:derby:prestupky_db;create=true";
        DBPopulator populator = new DBPopulator(this);
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
            if (!isDbPopulated()) populator.populateDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getDrivers() {
        return executeSQL(getDriverQuery(
                BinaryCondition.EMPTY, BinaryCondition.EMPTY));
    }

    public ResultSet getDriversWhoLostLicenseFromCity(String city) {
        return executeSQL(getDriverQuery(
                BinaryCondition.greaterThan(
                        FunctionCall.sum().addColumnParams(offensesTable.findColumn(POINT_COUNT)),
                        12, true),
                BinaryCondition.equalTo(driversTable.findColumn(CITY), city)));
    }

    public ResultSet getDriversFromTo(LocalDate[] dates) {
        return executeSQL(getDriverQuery(
                BinaryCondition.EMPTY,
                ComboCondition.and(
                        BinaryCondition.greaterThan(eventsTable.findColumn(DATE), dates[0], true),
                        BinaryCondition.lessThan(eventsTable.findColumn(DATE), dates[1], true))));
    }

    public ResultSet executeSQL(String SQL) {
        try {
            Statement statement = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
            return !(result != null && result.first());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private String getDriverQuery(Condition havingCondition, Condition condition) {
        return new SelectQuery()
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
    }

    private DbTable initializeDriversTable() {
        DbTable table = new DbTable(schema, DRIVERS);
        table.addColumn(ID, "BIGINT", null).primaryKey();
        table.addColumn(NAME, "VARCHAR", 50).notNull();
        table.addColumn(SURNAME, "VARCHAR", 50).notNull();
        table.addColumn(DATE_OF_BIRTH, "DATE", null).notNull();
        table.addColumn(SEX, "VARCHAR", 10).notNull();
        table.addColumn(CITY, "VARCHAR", 50).notNull();
        return table;
    }

    private DbTable initializeEventsTable() {
        DbTable table = new DbTable(schema, EVENTS);
        table.addColumn(ID, "BIGINT", null).primaryKey();
        table.addColumn(OFFENSE_ID, "BIGINT", null);
        table.addColumn(DRIVER_ID, "BIGINT", null);
        table.addColumn(POLICEMAN_ID, "BIGINT", null);
        table.addColumn(DATE, "DATE", null);
        table.addColumn(DESCRIPTION, "VARCHAR", 5000);
        return table;
    }

    private DbTable initializePolicemenTable() {
        DbTable table = new DbTable(schema, POLICEMEN);
        table.addColumn(ID, "BIGINT", null).primaryKey();
        table.addColumn(NAME, "VARCHAR", 50).notNull();
        table.addColumn(SURNAME, "VARCHAR", 50).notNull();
        table.addColumn(NUMBER, "BIGINT", null).notNull();
        table.addColumn(DEPARTMENT_ID, "BIGINT", null).notNull();
        return table;
    }

    private DbTable initializeDepartmentsTable() {
        DbTable table = new DbTable(schema, DEPARTMENTS);
        table.addColumn(ID, "BIGINT", null).primaryKey();
        table.addColumn(NAME, "VARCHAR", 50).notNull();
        table.addColumn(CITY, "VARCHAR", 50).notNull();
        return table;
    }

    private DbTable initializeOffensesTable() {
        DbTable table = new DbTable(schema, OFFENSES);
        table.addColumn(ID, "BIGINT", null).primaryKey();
        table.addColumn(POINT_COUNT, "SMALLINT", null).notNull();
        table.addColumn(NAME, "VARCHAR", 100).notNull();
        return table;
    }

    public ResultSet getPolicemenFromDepartmentWithinYear(String department, int year) {
        LocalDate start = LocalDate.ofYearDay(year, 1);
        LocalDate end = LocalDate.ofYearDay(year, start.isLeapYear() ? 366 : 365);
        Condition departmentCondition = BinaryCondition.equalTo(departmentsTable.findColumn(NAME), department);
        Condition comboCondition = ComboCondition.and(
                departmentCondition,
                BinaryCondition.greaterThan(eventsTable.findColumn(DATE), start, true),
                BinaryCondition.lessThan(eventsTable.findColumn(DATE), end, true));
        return getPolicemen(comboCondition);
    }

    public ResultSet getAllPolicemen() {
        return getPolicemen(BinaryCondition.EMPTY);
    }

    private ResultSet getPolicemen(Condition condition) {
        String policemenQuery = new SelectQuery()
                .addCustomColumns(
                        policemenTable.findColumn(NAME),
                        policemenTable.findColumn(SURNAME),
                        policemenTable.findColumn(NUMBER))
                .addAliasedColumn( FunctionCall.count().addColumnParams(eventsTable.findColumn(ID)), OFFENSES_COUNT)
                .addJoin(SelectQuery.JoinType.INNER,
                        eventsTable,
                        policemenTable,
                        BinaryCondition.equalTo(eventsTable.findColumn(POLICEMAN_ID), policemenTable.findColumn(ID)))
                .addJoin(SelectQuery.JoinType.INNER,
                        policemenTable,
                        departmentsTable,
                        BinaryCondition.equalTo(
                                policemenTable.findColumn(DEPARTMENT_ID),
                                departmentsTable.findColumn(ID)))
                .addCondition(condition)
                .addGroupings(
                        policemenTable.findColumn(NAME),
                        policemenTable.findColumn(SURNAME),
                        policemenTable.findColumn(NUMBER))
                .validate().toString();
        return executeSQL(policemenQuery);
    }

    public ResultSet getAllCities() {
        String citiesQuery = new SelectQuery(true)
                .addCustomColumns(driversTable.findColumn(CITY)).validate().toString();
        return executeSQL(citiesQuery);
    }

    private void createTable(DbTable table) {
        String createTable = new CreateTableQuery(table, true).validate().toString();
        if (isDbPopulated()) setDbPopulated(false); //set flag
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

    public ResultSet getDepartments(Condition condition) {
        String customQuery = new SelectQuery()
                .addCustomColumns(
                        departmentsTable.findColumn(CITY),
                        departmentsTable.findColumn(NAME),
                        departmentsTable.findColumn(ID))
                .addAliasedColumn(FunctionCall.count().addColumnParams(eventsTable.findColumn(ID)),
                        OFFENSES_COUNT)
                .addCustomJoin(SelectQuery.JoinType.INNER,
                        eventsTable, policemenTable,
                        BinaryCondition.equalTo(
                                eventsTable.findColumn(POLICEMAN_ID),
                                policemenTable.findColumn(ID)))
                .addCustomJoin(SelectQuery.JoinType.INNER,
                        policemenTable, departmentsTable,
                        BinaryCondition.equalTo(
                                policemenTable.findColumn(DEPARTMENT_ID),
                                departmentsTable.findColumn(ID)))
                .addCustomGroupings
                        (departmentsTable.findColumn(CITY),
                                departmentsTable.findColumn(NAME),
                                departmentsTable.findColumn(ID))
                .addCondition(condition)
                .validate().toString();
        return executeSQL(customQuery);
    }

    public ResultSet getAllDepartments() {
        return getDepartments(BinaryCondition.EMPTY);
    }

    public ResultSet getAllDepartmentsNames() {
        String query = new SelectQuery(true)
                .addColumns(departmentsTable.findColumns(NAME))
                .validate().toString();
        return executeSQL(query);
    }

    public ResultSet getDepartmentsWithinYear(int year) {
        LocalDate start = LocalDate.ofYearDay(year, 1);
        LocalDate end = LocalDate.ofYearDay(year, start.isLeapYear() ? 366 : 365);
        return getDepartments(ComboCondition.and(
                BinaryCondition.greaterThan(eventsTable.findColumn(DATE), start, true),
                BinaryCondition.lessThan(eventsTable.findColumn(DATE), end, true)));
    }

    public DbTable getOffensesTable() {
        return offensesTable;
    }

    public DbTable getDriversTable() {
        return driversTable;
    }

    public DbTable getPolicemenTable() {
        return policemenTable;
    }

    public DbTable getEventsTable() {
        return eventsTable;
    }

    public DbTable getDepartmentsTable() {
        return departmentsTable;
    }

    private String resultToString(ResultSet rs) {
        StringBuilder sb = new StringBuilder(2048);
        sb.append("\n");

        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                sb.append(rsmd.getColumnName(i)).append("\t");
            }
            sb.append("\n");
            while (rs.next()) {
                sb.append(String.format("%-10s%-10s%-10s%3s%3s%3s",
                        rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6))).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
