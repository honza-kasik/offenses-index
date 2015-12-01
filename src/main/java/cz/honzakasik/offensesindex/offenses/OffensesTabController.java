package cz.honzakasik.offensesindex.offenses;

import cz.honzakasik.offensesindex.DialogHelper;
import cz.honzakasik.offensesindex.Helper;
import cz.honzakasik.offensesindex.database.DBManager;
import cz.honzakasik.offensesindex.database.OffensesDBManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Jan Kasik on 14.11.15.
 */
public class OffensesTabController {

    @FXML TableView<OffensesTableItem> offensesTable;
    @FXML DatePicker dateFrom;
    @FXML DatePicker dateTo;
    @FXML ChoiceBox<String> pointCount;
    @FXML Button showOffensesButton;

    private StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
        String pattern = "MM.yyyy";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }

        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    };

    private OffensesDBManager offensesDBManager;
    private Stage parentStage;

    public void initialize(DBManager dbManager, Stage parentStage) {
        offensesDBManager = new OffensesDBManager(dbManager);
        this.parentStage = parentStage;
        offensesTable.setItems(offensesDBManager.getAllOffenses());
        pointCount.setItems(offensesDBManager.getDistinctOffensesCategories());
        dateFrom.setConverter(converter);
        dateTo.setConverter(converter);
        bindValidationSupport();
    }

    public void showOffensesWithinMonthsAction() {
        LocalDate from = dateFrom.getValue();
        from = from.withDayOfMonth(1);
        LocalDate to = dateTo.getValue();
        to = to.withDayOfMonth(to.lengthOfMonth());
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
                .registerValidator(dateFrom, Validator.createEmptyValidator("You must fill in the month!"));
        validationSupport
                .registerValidator(dateTo, Validator.createEmptyValidator("You must fill in the month!"));
        validationSupport.registerValidator(pointCount, Validator.createEmptyValidator("You must select category"));
        showOffensesButton.disableProperty().bind(validationSupport.invalidProperty());
    }

    public void resetViewButtonAction() {
        dateFrom.setValue(null);
        dateTo.setValue(null);
        pointCount.setValue(null);
        offensesTable.setItems(offensesDBManager.getAllOffenses());
    }
}
