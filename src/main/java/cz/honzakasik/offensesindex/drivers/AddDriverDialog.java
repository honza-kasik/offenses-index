package cz.honzakasik.offensesindex.drivers;

import cz.honzakasik.offensesindex.database.DBManager;
import cz.honzakasik.offensesindex.database.DriversDBManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.util.concurrent.ThreadLocalRandom;

import static cz.honzakasik.offensesindex.database.DatabaseNames.*;

public class AddDriverDialog {

    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;
    @FXML
    private TextField cityTextField;
    @FXML
    private ChoiceBox<String> genderChoiceBox;
    @FXML
    private TextField nameTextField;
    @FXML
    private DatePicker dateOfBirthDatePicker;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField streetTextField;

    private DriversDBManager driversDBManager;

    @FXML
    void cancelButtonAction(ActionEvent event) {
        closeDialog();
    }

    @FXML
    void confirmButtonAction(ActionEvent event) {
        Driver driver = new Driver(nameTextField.getText(),
                surnameTextField.getText(),
                cityTextField.getText(),
                streetTextField.getText(),
                ThreadLocalRandom.current().nextInt(),
                genderChoiceBox.getValue(),
                dateOfBirthDatePicker.getValue());
        driversDBManager.insertNewDriver(driver);
        closeDialog();
    }

    private void closeDialog() {
        confirmButton.getScene().getWindow().hide();
    }

    public void initialize(DBManager dbManager) {
        driversDBManager = new DriversDBManager(dbManager);
        genderChoiceBox.setItems(FXCollections.observableArrayList(MALE, FEMALE));
        bindValidationSupport();
    }

    private void bindValidationSupport() {
        String emptyMessage = "You have to fill this field";
        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.registerValidator(cityTextField,
                Validator.createEmptyValidator(emptyMessage));
        validationSupport.registerValidator(genderChoiceBox,
                Validator.createEmptyValidator(emptyMessage));
        validationSupport.registerValidator(nameTextField,
                Validator.createEmptyValidator(emptyMessage));
        validationSupport.registerValidator(dateOfBirthDatePicker,
                Validator.createEmptyValidator(emptyMessage));
        validationSupport.registerValidator(surnameTextField,
                Validator.createEmptyValidator(emptyMessage));
        validationSupport.registerValidator(streetTextField,
                Validator.createEmptyValidator(emptyMessage));
        confirmButton.disableProperty().bind(validationSupport.invalidProperty());
    }

}
