package cz.honzakasik.offensesindex;

import cz.honzakasik.offensesindex.database.DBManager;
import cz.honzakasik.offensesindex.database.DriversDBManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.time.LocalDate;

/**
 * Created by Jan Kasik on 28.10.15.
 */
public class DateSelector {

    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;

    private DriversDBManager driversDBManager;
    private boolean result = false;
    private ValidationSupport validationSupport = new ValidationSupport();

    public boolean getResult() {
        return result;
    }

    public void confirmButtonAction() {
        if (driversDBManager.getDriversFromTo(getDates()).isEmpty()) {
            DialogHelper.displayNothingFoundError((Stage)confirmButton.getScene().getWindow());
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

    public void initialize(DBManager dbManager) {
        driversDBManager = new DriversDBManager(dbManager);
        validationSupport.registerValidator(dateFrom, Validator.createEmptyValidator("Je třeba vyplnit datum od!"));
        validationSupport.registerValidator(dateTo, Validator.createEmptyValidator("Je třeba vyplnit datum do!"));
        confirmButton.disableProperty().bind(validationSupport.invalidProperty());
    }
}
