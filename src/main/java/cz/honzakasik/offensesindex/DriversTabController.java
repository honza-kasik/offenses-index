package cz.honzakasik.offensesindex;

import cz.honzakasik.offensesindex.database.DBManager;
import cz.honzakasik.offensesindex.database.DriversDBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Jan Kasik on 14.11.15.
 */
public class DriversTabController {

    @FXML private TableView<DriverTableItem> driversTable;
    @FXML private CheckBox lostLicenseSwitch;

    private DriversDBManager driversDBManager;

    public void setDriversDBManager(DBManager driversDBManager) {
        this.driversDBManager = new DriversDBManager(driversDBManager);
    }

    public void displayDateSelector(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("selectDateSection.fxml"));
        Parent root = loader.load();

        DateSelector controller = loader.getController();
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.initOwner(null);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.setTitle("Zvolte časový úsek pro zobrazení přestupků");
        stage.setScene(scene);

        stage.setOnHidden(event -> {
            if (controller.getResult())
                driversTable.setItems(Helper.transformDriverTableData(driversDBManager.getDriversFromTo(controller.getDates())));
        });
        stage.showAndWait();
    }

    public void displayDriversWhoLostLicenseSwitch(ActionEvent actionEvent) throws IOException {
        if (lostLicenseSwitch.isSelected()) {
            displayCitySelector();
        } else {
            driversTable.setItems(Helper.transformDriverTableData(driversDBManager.getDrivers()));
        }
    }

    private void displayCitySelector() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("cityChoser.fxml"));
        Parent root = loader.load();

        CitySelector controller = loader.getController();
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.initOwner(null);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.setTitle("Zvolte město");
        stage.setScene(scene);

        stage.setOnHidden(event -> {
            String result = controller.getResult();
            if (result != null && !result.isEmpty()) {
                driversTable.setItems(Helper.transformDriverTableData(driversDBManager.getDriversWhoLostLicenseFromCity(result)));
            }
        });
        controller.setItems(Helper.transformCitySelectorData(null));
        stage.showAndWait();
    }

    public void addDriverAction(ActionEvent actionEvent) {
    }

    public void initialize(DBManager dbManager) {
        setDriversDBManager(dbManager);
        driversTable.setItems(Helper.transformDriverTableData(driversDBManager.getDrivers()));
    }

}
