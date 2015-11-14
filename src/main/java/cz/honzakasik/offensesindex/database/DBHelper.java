package cz.honzakasik.offensesindex.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by Jan Kasik on 14.11.15.
 */
public class DBHelper {

    private static String resultToString(ResultSet rs) {
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


    public static boolean isResultEmpty(ResultSet result) {
        try {
            return !(result != null && result.first());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
