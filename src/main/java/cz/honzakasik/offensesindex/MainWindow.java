package cz.honzakasik.offensesindex;

import cz.honzakasik.offensesindex.database.DBManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jan Kasik on 28.10.15.
 */
public class MainWindow extends Application implements Initializable {

    private static final Logger logger = Logger.getLogger(MainWindow.class.getName());
    private Stage primaryStage;
    private final DBManager dbManager = new DBManager();
    private final ValidationSupport validationSupportDepartment = new ValidationSupport();
    private final ValidationSupport validationSupportPoliceman = new ValidationSupport();
    @FXML private TableView<DriverTableItem> driversTable;
    @FXML private TableView<DepartmentTableItem> departmentTable;
    @FXML private TableView<PolicemanTableItem> policemenTable;
    @FXML private CheckBox lostLicenseSwitch;
    @FXML private Button showOffensesWithinYear;
    @FXML private Button findPolicemenButton;
    @FXML private TextField offensesYear;
    @FXML private TextField policemenYear;
    @FXML private ComboBox<String> policemenDepartmentSelector;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("mainWindow.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Evidence přestupků");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        logger.log(Level.INFO, "MainWindow's stage showed!");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        driversTable.setItems(Helper.transformDriverTableData(dbManager.getDrivers()));
        departmentTable.setItems(Helper.transformDepartmentTableData(dbManager.getAllDepartments()));
        policemenTable.setItems(Helper.transformPolicemenTableData(dbManager.getAllPolicemen()));
        policemenDepartmentSelector.setItems(Helper.transformDepartmentSelectorData(dbManager.getAllDepartmentsNames()));
        bindDepartmentValidationSupport();
        bindPolicemenValidationSupport();
    }

    private void bindDepartmentValidationSupport() {
        validationSupportDepartment
                .registerValidator(offensesYear, (Control c, String newValue) ->
                        ValidationResult.fromErrorIf(c, "You must fill the year!", !Helper.isYear(newValue)));
        showOffensesWithinYear.disableProperty().bind(validationSupportDepartment.invalidProperty());
    }

    private void bindPolicemenValidationSupport() {
        ValidationSupport.setRequired(policemenDepartmentSelector, true);
         validationSupportPoliceman
                .registerValidator(policemenDepartmentSelector,
                        Validator.createEmptyValidator("bla"));
        validationSupportPoliceman
                .registerValidator(policemenYear, (Control c, String newValue) ->
                        ValidationResult.fromErrorIf(c, "You must fill the year!", !Helper.isYear(newValue)));
        findPolicemenButton.disableProperty().bind(validationSupportPoliceman.invalidProperty());
    }

    public void displayDateSelector(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("selectDateSection.fxml"));
        Parent root = loader.load();

        DateSelector controller = loader.getController();
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.setTitle("Zvolte časový úsek pro zobrazení přestupků");
        stage.setScene(scene);

        stage.setOnHidden(event -> {
            if (controller.getResult())
                driversTable.setItems(Helper.transformDriverTableData(dbManager.getDriversFromTo(controller.getDates())));
            });
        stage.showAndWait();
    }

    public void loadActualInformation(Event event) {
    }

    public void displayDriversWhoLostLicenseSwitch(ActionEvent actionEvent) throws IOException {
        if (lostLicenseSwitch.isSelected()) {
            displayCitySelector();
        } else {
            driversTable.setItems(Helper.transformDriverTableData(dbManager.getDrivers()));
        }
    }

    private void displayCitySelector() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("cityChoser.fxml"));
        Parent root = loader.load();

        CitySelector controller = loader.getController();
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.setTitle("Zvolte město");
        stage.setScene(scene);

        stage.setOnHidden(event -> {
            String result = controller.getResult();
            if (result != null && !result.isEmpty()) {
                driversTable.setItems(Helper.transformDriverTableData(dbManager.getDriversWhoLostLicenseFromCity(result)));
            }
        });
        controller.setItems(Helper.transformCitySelectorData(dbManager.getAllCities()));
        stage.showAndWait();
    }

    public void addDriverAction(ActionEvent actionEvent) {
    }

    public void showOffensesWithinYearAction(ActionEvent actionEvent) {
        String text = offensesYear.getText();
        int year = Integer.valueOf(text);
        ResultSet results = dbManager.getDepartmentsWithinYear(year);
        if (dbManager.isResultEmpty(results)) {
            Helper.displayNothingFoundError(primaryStage);
        } else {
            departmentTable.setItems(Helper.transformDepartmentTableData(results));
        }
    }


    public void displayPolicemenFromDepartmentWithinYear(ActionEvent actionEvent) {
        int year = Integer.valueOf(policemenYear.getText());
        ResultSet result = dbManager.getPolicemenFromDepartmentWithinYear(policemenDepartmentSelector.getValue(), year);
        policemenTable.setItems(Helper.transformPolicemenTableData(result));
        if (dbManager.isResultEmpty(result)) {
            Helper.displayNothingFoundError(primaryStage);
            policemenYear.clear();
            policemenDepartmentSelector.setValue(null);
            policemenTable.setItems(Helper.transformPolicemenTableData(dbManager.getAllPolicemen()));
        }
    }

    public void showOffensesWithinMonths(ActionEvent actionEvent) {
    }
}
