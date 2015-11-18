package cz.honzakasik.offensesindex.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by Jan Kasik on 14.11.15.
 */
public abstract class DBHelper {

    public static String resultToString(ResultSet rs) {
        StringBuilder sb = new StringBuilder(2048);
        sb.append("\n");

        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                sb.append(String.format("%-10s", metaData.getColumnName(i)));
            }
            sb.append("\n");
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnValue = rs.getObject(i).toString();
                    sb.append(String.format("%-10s", columnValue));
                }
                sb.append("\n");
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

    public static ObservableList<String> transformStringData(ResultSet result, String name) {
        ObservableList<String> data = FXCollections.observableArrayList();
        try {
            while (result != null && result.next()) {
                data.add(result.getObject(name).toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
