package cz.honzakasik.offensesindex;

import java.sql.*;
import java.time.LocalDate;

import static cz.honzakasik.offensesindex.DatabaseNames.*;
/**
 * Created by Jan Kasik on 28.10.15.
 */
public class Dispatcher {

    private Connection connection;

    public Dispatcher() {
        String url = "jdbc:postgresql://localhost/";
        try {
            this.connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getDrivers() {
        return executeSQL(getDriverQuery(""));
    }

    public ResultSet getDriversFromCity(String city) {
        return executeSQL(getDriverQuery("AND " + DRIVERS + "." + CITY + "=" + city));
    }

    public ResultSet getDriversFromTo(LocalDate[] dates) {
        return executeSQL(getDriverQuery(   "AND " + EVENTS + "." + DATE + ">=" + dates[0] +
                                            "AND " + EVENTS + "." + DATE + "<=" + dates[1]));
    }

    private ResultSet executeSQL(String SQL) {
        try {
            Statement statement = connection.createStatement();
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
        return  "SELECT count(" + EVENTS + "." + ID + ") AS " + OFFENSES_COUNT + "," + "\n" +
                    "sum(" + OFFENSES + "." + POINT_COUNT + ") as " + POINT_COUNT + "," + "\n" +
                    DRIVERS + "." + NAME + " AS " + NAME + "," + "\n" +
                    DRIVERS + "." + SURNAME + " AS " + SURNAME + "," + "\n" +
                    DRIVERS + "." + CITY + " AS " + CITY + "\n" +
                    "FROM " + EVENTS + "\n" +
                    "JOIN " + DRIVERS + " ON " + EVENTS + "." + DRIVER_ID + " = " + DRIVERS + "." + ID + "\n" +
                        condition + "\n" +
                    "JOIN " + OFFENSES + " ON " + EVENTS + "." + OFFENSE_ID + " = " + OFFENSES + "." + ID + "\n" +
                    "GROUP BY " + NAME + ", " + SURNAME + ", " + CITY + ";";
    }
}
