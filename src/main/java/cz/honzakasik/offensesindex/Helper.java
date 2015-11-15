package cz.honzakasik.offensesindex;

import cz.honzakasik.offensesindex.departments.DepartmentTableItem;
import cz.honzakasik.offensesindex.drivers.DriverTableItem;
import cz.honzakasik.offensesindex.policemen.PolicemanTableItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cz.honzakasik.offensesindex.database.DatabaseNames.*;
import static cz.honzakasik.offensesindex.database.DatabaseNames.CITY;

/**
 * Created by Jan Kasik on 4.11.15.
 */
public abstract class Helper {

    private static Logger logger = Logger.getLogger(Helper.class.toString());

    public static ObservableList<DriverTableItem> transformDriverTableData(ResultSet result) {
        ObservableList<DriverTableItem> data = FXCollections.observableArrayList();
        try {
            while (result != null && result.next()) {
                DriverTableItem driverTableItem = new DriverTableItem(
                        result.getString(NAME),
                        result.getString(SURNAME),
                        result.getInt(POINT_COUNT),
                        result.getInt(OFFENSES_COUNT),
                        result.getString(CITY),
                        result.getInt(ID));
                data.add(driverTableItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static ObservableList<String> transformCitySelectorData(ResultSet result) {
        return transformStringData(result, CITY);
    }

    public static ObservableList<String> transformDepartmentSelectorData(ResultSet result) {
        return transformStringData(result, NAME);
    }

    public static ObservableList<String> transformStringData(ResultSet result, String name) {
        ObservableList<String> data = FXCollections.observableArrayList();
        try {
            while (result != null && result.next()) {
                data.add(result.getString(name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static ObservableList<DepartmentTableItem> transformDepartmentTableData(ResultSet result) {
        ObservableList<DepartmentTableItem> data = FXCollections.observableArrayList();
        try {
            while (result != null && result.next()) {
                DepartmentTableItem item = new DepartmentTableItem(
                        result.getString(NAME),
                        result.getString(CITY),
                        result.getInt(OFFENSES_COUNT),
                        result.getInt(ID)
                );
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, data.toString() + "\n");
        return data;
    }

    public static void displayNothingFoundError(Stage owner) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(owner);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.setTitle("Nebyly nalezeny žádné výsledky");
        alert.setHeaderText("Pro zadané parametry nebyly nalezeny žádné výsledky!");
        alert.setContentText("Opakujte prosím akci s jinými parametry.");
        alert.showAndWait();
    }

    public static boolean isYear(String str)
    {
        return str.matches("[1-9]\\d{3}");  //match a number starting with 1-9
    }

    public static ObservableList<PolicemanTableItem> transformPolicemenTableData(ResultSet result) {
        ObservableList<PolicemanTableItem> data = FXCollections.observableArrayList();
        try {
            while (result != null && result.next()) {
                PolicemanTableItem item = new PolicemanTableItem(
                        result.getString(NAME),
                        result.getString(SURNAME),
                        result.getInt(NUMBER),
                        result.getInt(OFFENSES_COUNT)
                );
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
