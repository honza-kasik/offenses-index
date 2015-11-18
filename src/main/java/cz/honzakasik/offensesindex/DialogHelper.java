package cz.honzakasik.offensesindex;

import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Jan Kasik on 18.11.15.
 */
public abstract class DialogHelper {

    public static void displayNothingFoundError(Stage owner) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(owner);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.setTitle("Nebyly nalezeny žádné výsledky");
        alert.setHeaderText("Pro zadané parametry nebyly nalezeny žádné výsledky!");
        alert.setContentText("Opakujte prosím akci s jinými parametry.");
        alert.showAndWait();
    }
}
