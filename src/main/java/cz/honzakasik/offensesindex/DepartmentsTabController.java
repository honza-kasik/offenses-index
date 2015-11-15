package cz.honzakasik.offensesindex;

import cz.honzakasik.offensesindex.database.DBHelper;
import cz.honzakasik.offensesindex.database.DBManager;
import cz.honzakasik.offensesindex.database.DepartmentsDBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;

import java.sql.ResultSet;

/**
 * Created by Jan Kasik on 14.11.15.
 */
public class DepartmentsTabController {

    @FXML private TableView<DepartmentTableItem> departmentTable;
    @FXML private Button showOffensesWithinYear;
    @FXML private TextField offensesYear;

    private Stage parentStage;
    private DepartmentsDBManager departmentsDBManager;
    private ValidationSupport validationSupport = new ValidationSupport();

    public void showOffensesWithinYearAction() {
        String text = offensesYear.getText();
        int year = Integer.valueOf(text);
        ResultSet results = departmentsDBManager.getDepartmentsWithinYear(year);
        if (DBHelper.isResultEmpty(results)) {
            Helper.displayNothingFoundError(parentStage);
        } else {
            departmentTable.setItems(Helper.transformDepartmentTableData(results));
        }
    }

    public void initialize(DBManager dbManager, Stage parentStage) {
        this.parentStage = parentStage;
        departmentsDBManager = new DepartmentsDBManager(dbManager);
        departmentTable.setItems(Helper.transformDepartmentTableData(departmentsDBManager.getAllDepartments()));
        bindDepartmentValidationSupport();
    }

    private void bindDepartmentValidationSupport() {
        validationSupport
                .registerValidator(offensesYear, (Control c, String newValue) ->
                        ValidationResult.fromErrorIf(c, "You must fill the year!", !Helper.isYear(newValue)));
        showOffensesWithinYear.disableProperty().bind(validationSupport.invalidProperty());
    }


}
