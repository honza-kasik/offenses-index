package cz.honzakasik.offensesindex;

import cz.honzakasik.offensesindex.database.DBManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Created by Jan Kasik on 28.10.15.
 */
public class DateSelector implements Initializable {

    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;

    private boolean result = false;
    private ValidationSupport validationSupport = new ValidationSupport();

    public boolean getResult() {
        return result;
    }

    public void confirmButtonAction() {
        DBManager DBManager = new DBManager();
        if (DBManager.isResultEmpty(DBManager.getDriversFromTo(getDates()))) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nebyly nalezeny žádné výsledky");
            alert.setHeaderText("Pro zadané parametry nebyly nalezeny žádné výsledky!");
            alert.setContentText("Opakujte prosím akci s jinými parametry.");
            alert.showAndWait();
        } else {
            result = true;
            closeDialog();
        }
    }

    public void closeDialog() {
        cancelButton.getScene().getWindow().hide();
    }

    public LocalDate[] getDates() {
        return new LocalDate[]{dateFrom.getValue(), dateTo.getValue()};
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validationSupport.registerValidator(dateFrom, Validator.createEmptyValidator("Je třeba vyplnit datum od!"));
        validationSupport.registerValidator(dateTo, Validator.createEmptyValidator("Je třeba vyplnit datum do!"));
        confirmButton.disableProperty().bind(validationSupport.invalidProperty());
    }
}
