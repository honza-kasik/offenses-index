
import java.sql.*;
import java.time.LocalDate;

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
        ResultSet result = executeSQL("SELECT count(udalosti.ID) as pocet_prestupku," +
                "sum(prestupky.body) as pocet_bodu," +
                "ridici.jmeno," +
                "ridici.prijmeni," +
                "ridici.mesto" +
                "FROM udalosti" +
                "JOIN ridici ON udalosti.ID_ridice = ridici.ID" +
                "JOIN prestupky ON udalosti.ID_prestupku = prestupky.ID" +
                "GROUP BY ridici.jmeno, ridici.prijmeni, ridici.mesto;");
        return result;
    }

    public ResultSet getDriversFromCity(String city) {
        ResultSet result = executeSQL("SELECT count(udalosti.ID) as pocet_prestupku," +
                "sum(prestupky.body) as pocet_bodu," +
                "ridici.jmeno," +
                "ridici.prijmeni," +
                "ridici.mesto" +
                "FROM udalosti" +
                "JOIN ridici ON udalosti.ID_ridice = ridici.ID" +
                    "AND ridici.mesto=" + city +
                "JOIN prestupky ON udalosti.ID_prestupku = prestupky.ID" +
                "GROUP BY ridici.jmeno, ridici.prijmeni, ridici.mesto;");
        return result;
    }

    public ResultSet getDriversFromTo(LocalDate[] dates) {
        ResultSet result = executeSQL("SELECT count(udalosti.ID) as pocet_prestupku," +
                "sum(prestupky.body) as pocet_bodu," +
                "ridici.jmeno," +
                "ridici.prijmeni," +
                "ridici.mesto" +
                "FROM udalosti" +
                "JOIN ridici ON udalosti.ID_ridice = ridici.ID" +
                    "AND udalosti.datum>=" + dates[0] + " AND udalosti.datum<=" + dates[1] +
                "JOIN prestupky ON udalosti.ID_prestupku = prestupky.ID" +
                "GROUP BY ridici.jmeno, ridici.prijmeni, ridici.mesto;");
        return result;
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
}
