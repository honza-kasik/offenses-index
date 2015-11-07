package cz.honzakasik.offensesindex;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    @FXML private TableView<DriverTableItem> driversTable;
    @FXML private CheckBox lostLicenseSwitch;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("Evidence přestupků");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        logger.log(Level.INFO, "MainWindow's stage showed!");
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void displayDateSelector(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("selectDateSection.fxml"));
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

    public void displayPoliceDepartmentsOrderedByOffenses(ActionEvent actionEvent) {
    }

    public void displayDriversWhoLostLicense(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("driversTable.fxml"));
        Parent root = loader.load();

        DriversTable controller = loader.getController();
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.setTitle("Přestupky řidičů");
        stage.setScene(scene);
        controller.initialize();

        stage.showAndWait();
    }

    public void displayWorstAndBestPoliceman(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("I have a great message for you!");

        alert.showAndWait();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cityChoser.fxml"));
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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        driversTable.setItems(Helper.transformDriverTableData(dbManager.getDrivers()));
    }
}
