package cz.honzakasik.offensesindex;

import cz.honzakasik.offensesindex.database.DBHelper;
import cz.honzakasik.offensesindex.database.DBManager;
import cz.honzakasik.offensesindex.database.DepartmentsDBManager;
import cz.honzakasik.offensesindex.database.PolicemenDBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.sql.ResultSet;

/**
 * Created by Jan Kasik on 14.11.15.
 */
public class PolicemenTabController {

    @FXML private Button findPolicemenButton;
    @FXML private TextField policemenYear;
    @FXML private ComboBox<String> policemenDepartmentSelector;
    @FXML private TableView<PolicemanTableItem> policemenTable;

    private ValidationSupport validationSupport = new ValidationSupport();
    private PolicemenDBManager policemenDBManager;

    public void initialize(DBManager dbManager) {
        policemenDBManager = new PolicemenDBManager(dbManager);
        DepartmentsDBManager departmentsDBManager = new DepartmentsDBManager(dbManager);
        policemenTable.setItems(Helper.transformPolicemenTableData(policemenDBManager.getAllPolicemen()));
        policemenDepartmentSelector.setItems(Helper.transformDepartmentSelectorData(departmentsDBManager.getAllDepartmentsNames()));
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

    public void displayPolicemenFromDepartmentWithinYear() {
        int year = Integer.valueOf(policemenYear.getText());
        ResultSet result = policemenDBManager.getPolicemenFromDepartmentWithinYear(policemenDepartmentSelector.getValue(), year);
        policemenTable.setItems(Helper.transformPolicemenTableData(result));
        if (DBHelper.isResultEmpty(result)) {
            Helper.displayNothingFoundError((Stage)findPolicemenButton.getScene().getWindow());
            policemenYear.clear();
            policemenDepartmentSelector.setValue(null);
            policemenTable.setItems(Helper.transformPolicemenTableData(policemenDBManager.getAllPolicemen()));
        }
    }
}
