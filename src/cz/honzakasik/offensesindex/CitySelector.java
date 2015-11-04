package cz.honzakasik.offensesindex;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jan Kasik on 4.11.15.
 */
public class CitySelector implements Initializable {

    @FXML private ChoiceBox<String> citySelector;
    @FXML private Button confirmButton;
    private String result;
    private ValidationSupport validationSupport = new ValidationSupport();

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void cancelAction(ActionEvent actionEvent) {
        result = null;
        closeDialog();
    }

    public void confirmAction(ActionEvent actionEvent) {
        setResult(citySelector.getValue());
        closeDialog();
    }

    private void closeDialog() {
        citySelector.getScene().getWindow().hide();
    }

    public void setItems(ObservableList<String> items) {
        citySelector.setItems(items);
        citySelector.setValue(items.get(0));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validationSupport.registerValidator(citySelector, Validator.createEmptyValidator("You must select city"));
        confirmButton.disableProperty().bind(validationSupport.invalidProperty());
    }
}