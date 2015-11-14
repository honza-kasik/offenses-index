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
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jan Kasik on 28.10.15.
 */
public class MainWindowController extends Application implements Initializable {

    private static final Logger logger = Logger.getLogger(MainWindowController.class.getName());
    private Stage primaryStage;
    private final DBManager dbManager = new DBManager();

    @FXML private OffensesTabController offensesTabController;
    @FXML private DriversTabController driversTabController;
    @FXML private PolicemenTabController policemenTabController;
    @FXML private DepartmentsTabController departmentsTabController;


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
        logger.log(Level.INFO, "MainWindowController's stage showed!");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        policemenTabController.initialize(dbManager);
        driversTabController.initialize(dbManager);
        departmentsTabController.initialize(dbManager);
    }


    public void loadActualInformation(Event event) {
    }

    public void showOffensesWithinMonths(ActionEvent actionEvent) {

    }
}
