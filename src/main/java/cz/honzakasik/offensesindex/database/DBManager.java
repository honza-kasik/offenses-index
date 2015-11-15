package cz.honzakasik.offensesindex.database;

import com.healthmarketscience.sqlbuilder.*;
import com.healthmarketscience.sqlbuilder.dbspec.basic.*;
import cz.honzakasik.offensesindex.database.populator.DBPopulator;

import java.sql.*;
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
    private static Logger logger = Logger.getLogger(DBManager.class.getName());
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
        String url = "jdbc:derby:prestupky_db;create=true";
        DBPopulator populator = new DBPopulator(this);
        try {
            connection = DriverManager.getConnection(url);
            if (!isTableReady(DRIVERS)) createTable(driversTable);
            if (!isTableReady(EVENTS)) createTable(eventsTable);
            if (!isTableReady(OFFENSES)) createTable(offensesTable);
            if (!isTableReady(DEPARTMENTS)) createTable(departmentsTable);
            if (!isTableReady(POLICEMEN)) createTable(policemenTable);
            if (!isDbPopulated()) populator.populateDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    private void createTable(DbTable table) {
        String createTable = new CreateTableQuery(table, true).validate().toString();
        if (isDbPopulated()) setDbPopulated(false);
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

    public ResultSet getAllCities() {
        String citiesQuery = new SelectQuery(true)
                .addCustomColumns(driversTable.findColumn(CITY)).validate().toString();
        return executeSQL(citiesQuery);
    }
}
