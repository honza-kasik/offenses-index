package cz.honzakasik.offensesindex;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

import static cz.honzakasik.offensesindex.database.DatabaseNames.*;
import static cz.honzakasik.offensesindex.database.DatabaseNames.CITY;

/**
 * Created by Jan Kasik on 4.11.15.
 */
public abstract class Helper {

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
                data.add(result.getObject(name).toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public static boolean isMonth(String string) {
        return string.matches("(0?[1-9]|1[012])");
    }

}
