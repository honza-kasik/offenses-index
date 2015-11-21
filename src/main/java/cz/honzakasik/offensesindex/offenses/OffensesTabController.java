package cz.honzakasik.offensesindex.offenses;

import cz.honzakasik.offensesindex.DialogHelper;
import cz.honzakasik.offensesindex.Helper;
import cz.honzakasik.offensesindex.database.DBManager;
import cz.honzakasik.offensesindex.database.OffensesDBManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * Created by Jan Kasik on 14.11.15.
 */
public class OffensesTabController {

    @FXML TableView<OffensesTableItem> offensesTable;
    @FXML TextField monthFrom;
    @FXML TextField monthTo;
    @FXML ChoiceBox<String> pointCount;
    @FXML Button showOffensesButton;

    private OffensesDBManager offensesDBManager;
    private Stage parentStage;

    public void initialize(DBManager dbManager, Stage parentStage) {
        offensesDBManager = new OffensesDBManager(dbManager);
        this.parentStage = parentStage;
        offensesTable.setItems(offensesDBManager.getAllOffenses());
        pointCount.setItems(offensesDBManager.getDistinctOffensesCategories());
        bindValidationSupport();
    }

    public void showOffensesWithinMonthsAction() {
        int from = Integer.valueOf(monthFrom.getText());
        int to = Integer.valueOf(monthTo.getText());
        int points = Integer.valueOf(pointCount.getValue());
        ObservableList<OffensesTableItem> data = offensesDBManager.getOffensesWithinMonths(from, to, points);
        if (data.isEmpty()) {
            DialogHelper.displayNothingFoundError(parentStage);
        } else {
            offensesTable.setItems(data);
        }
    }

    private void bindValidationSupport() {
        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport
                .registerValidator(monthFrom, (Control c, String newValue) ->
                        ValidationResult.fromErrorIf(c, "You must fill the month!", !Helper.isMonth(newValue)));
        validationSupport
                .registerValidator(monthTo, (Control c, String newValue) ->
                        ValidationResult.fromErrorIf(c, "You must fill the month!", !Helper.isMonth(newValue)));
        validationSupport.registerValidator(pointCount, Validator.createEmptyValidator("You must select category"));
        showOffensesButton.disableProperty().bind(validationSupport.invalidProperty());
    }
}
