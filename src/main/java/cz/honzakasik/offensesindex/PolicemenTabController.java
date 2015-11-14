package cz.honzakasik.offensesindex;

import cz.honzakasik.offensesindex.database.DBHelper;
import cz.honzakasik.offensesindex.database.DBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

/**
 * Created by Jan Kasik on 14.11.15.
 */
public class PolicemenTabController {

    @FXML private Button findPolicemenButton;
    @FXML private TextField policemenYear;
    @FXML private ComboBox<String> policemenDepartmentSelector;
    @FXML private TableView<PolicemanTableItem> policemenTable;

    private ValidationSupport validationSupport = new ValidationSupport();
    private DBManager dbManager;


    public void setDbManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public void initialize(DBManager dbManager) {
        setDbManager(dbManager);
        policemenTable.setItems(Helper.transformPolicemenTableData(dbManager.getAllPolicemen()));
        policemenDepartmentSelector.setItems(Helper.transformDepartmentSelectorData(dbManager.getAllDepartmentsNames()));
        bindPolicemenValidationSupport();
    }

    private void bindPolicemenValidationSupport() {
        ValidationSupport.setRequired(policemenDepartmentSelector, true);
        validationSupport
                .registerValidator(policemenDepartmentSelector,
                        Validator.createEmptyValidator("bla"));
        validationSupport
                .registerValidator(policemenYear, (Control c, String newValue) ->
                        ValidationResult.fromErrorIf(c, "You must fill the year!", !Helper.isYear(newValue)));
        findPolicemenButton.disableProperty().bind(validationSupport.invalidProperty());
    }

    public void displayPolicemenFromDepartmentWithinYear(ActionEvent actionEvent) {
        int year = Integer.valueOf(policemenYear.getText());
        ResultSet result = dbManager.getPolicemenFromDepartmentWithinYear(policemenDepartmentSelector.getValue(), year);
        policemenTable.setItems(Helper.transformPolicemenTableData(result));
        if (DBHelper.isResultEmpty(result)) {
            Helper.displayNothingFoundError((Stage)findPolicemenButton.getScene().getWindow());
            policemenYear.clear();
            policemenDepartmentSelector.setValue(null);
            policemenTable.setItems(Helper.transformPolicemenTableData(dbManager.getAllPolicemen()));
        }
    }
}
