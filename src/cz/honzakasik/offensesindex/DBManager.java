package cz.honzakasik.offensesindex;

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
    private Logger logger = Logger.getLogger(DBManager.class.getName());

    public DBManager() {
        String url = "jdbc:derby:prestupky_db;create=true";
        try {
            this.connection = DriverManager.getConnection(url);
            if (!isTableReady(DRIVERS))
                createDriversTable();
            if (!isTableReady(EVENTS))
                createEventsTable();
            boolean bla = isTableReady(EVENTS);
            if (bla == true) bla = true;
            if (!isTableReady(OFFENSES))
                createOffensesTable();
            if (!isTableReady(DEPARTMENTS))
                createPoliceDepartmentsTable();
            if (!isTableReady(POLICEMEN))
                createPolicemenTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getDrivers() {
        return executeSQL(getDriverQuery(""));
    }

    public ResultSet getDriversFromCity(String city) {
        return executeSQL(getDriverQuery(" AND " + DRIVERS + "." + CITY + "=" + city));
    }

    public ResultSet getDriversFromTo(LocalDate[] dates) {
        return executeSQL(getDriverQuery(   " AND " + EVENTS + "." + DATE + ">=" + dates[0] +
                                            " AND " + EVENTS + "." + DATE + "<=" + dates[1]));
    }

    private ResultSet executeSQL(String SQL) {
        try {
            Statement statement = connection.createStatement();
            logger.log(Level.WARNING, "Executing: " + SQL);
            statement.execute(SQL);
            return statement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isResultEmpty(ResultSet result) {
        try {
            return !result.isBeforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private String getDriverQuery(String condition) {
        return  "SELECT count(\"" + dot(EVENTS, ID) +  "\") AS \"" + OFFENSES_COUNT + "\"," + "\n" +
                    "sum(\"" + dot(OFFENSES, POINT_COUNT) + "\") as \"" + POINT_COUNT + "\"," + "\n" +
                    "\"" + dot(DRIVERS, NAME) + "\" AS \"c" + NAME + "\"," + "\n" +
                    "\"" + dot(DRIVERS, SURNAME) + "\" AS \"c" + SURNAME + "\"," + "\n" +
                    "\"" + dot(DRIVERS, CITY) + "\" AS \"c" + CITY + "\"\n" +
                    "FROM \"" + EVENTS + "\"\n" +
                    "JOIN \"" + DRIVERS + "\" ON \"" + dot(EVENTS, DRIVER_ID) + "\" = \"" + dot(DRIVERS, ID) + "\"\n" +
                        condition + "\n" +
                    "JOIN \"" + OFFENSES + "\" ON \"" + dot(EVENTS, OFFENSE_ID) + "\" = \"" + dot(OFFENSES, ID) + "\"\n" +
                    "GROUP BY \"c" + NAME + "\", \"c" + SURNAME + "\", \"c" + CITY + "\"";
    }

    private void createDriversTable() {
        executeSQL("CREATE TABLE \"" + DRIVERS + "\"(" +
                "\"" + ID + "\" NUMERIC(2) NOT NULL PRIMARY KEY," +
                "\"" + NAME + "\" VARCHAR(2) NOT NULL," +
                "\"" + SURNAME + "\" VARCHAR(2) NOT NULL," +
                "\"" + DATE_OF_BIRTH + "\" DATE NOT NULL," +
                "\"" + SEX + "\" VARCHAR(10) NOT NULL," +
                "\"" + CITY + "\" VARCHAR(20) NOT NULL," +
                "\"" + POINT_COUNT + "\" NUMERIC(2) NOT NULL)");
    }

    private void createEventsTable() {
        executeSQL("CREATE TABLE \"" + EVENTS + "\"(" +
                "\"" + ID + "\" NUMERIC(2) NOT NULL PRIMARY KEY," +
                "\"" + OFFENSE_ID + "\" NUMERIC(2) NOT NULL," +
                "\"" + DRIVER_ID + "\" NUMERIC(2) NOT NULL," +
                "\"" + POLICEMAN_ID + "\" NUMERIC(2) NOT NULL," +
                "\"" + DATE + "\" DATE NOT NULL," +
                "\"" + DESCRIPTION + "\" VARCHAR(500) NOT NULL)");
    }

    private void createPolicemenTable() {
        executeSQL("CREATE TABLE \"" + POLICEMEN + "\"(" +
                "\"" + ID + "\" NUMERIC(2) NOT NULL PRIMARY KEY," +
                "\"" + NAME + "\" VARCHAR(20) NOT NULL," +
                "\"" + SURNAME + "\" VARCHAR(20) NOT NULL," +
                "\"" + NUMBER + "\" NUMERIC(2) NOT NULL," +
                "\"" + DEPARTMENT_ID + "\" NUMERIC(2))");
    }

    private void createPoliceDepartmentsTable() {
        executeSQL("CREATE TABLE \"" + DEPARTMENTS + "\"(" +
                "\"" + ID + "\" NUMERIC(2) NOT NULL PRIMARY KEY," +
                "\"" + NAME + "\" VARCHAR(100) NOT NULL," +
                "\"" + CITY + "\" VARCHAR(2) NOT NULL)");
    }

    private void createOffensesTable() {
        executeSQL("CREATE TABLE \"" + OFFENSES + "\"(" +
                "\"" + ID + "\" NUMERIC(2) NOT NULL PRIMARY KEY," +
                "\"" + POINT_COUNT + "\" NUMERIC(2) NOT NULL," +
                "\"" + NAME + "\" VARCHAR(2) NOT NULL)");
    }

    private boolean isTableReady(String table) {
        try {
            DatabaseMetaData dbm = connection.getMetaData();
            try (ResultSet tables = dbm.getTables(null, null, table, null)) {
                return tables.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String dot(String one, String two) {
        return one + "\".\"" + two;
    }
}
