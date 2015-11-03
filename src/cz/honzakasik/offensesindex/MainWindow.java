package cz.honzakasik.offensesindex;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jan Kasik on 28.10.15.
 */
public class MainWindow extends Application {

    private static final Logger logger = Logger.getLogger(MainWindow.class.getName());
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("mainWindows.fxml"));
        primaryStage.setTitle("Evidence přestupků");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        logger.log(Level.FINEST, "MainWindow's stage showed!");
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
            if (controller.getResult()) {
                try {
                    showDriversTable(controller.getDates());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        stage.showAndWait();
    }

    private void showDriversTable(LocalDate[] dates) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("driversTable.fxml"));
        Parent root = loader.load();

        DriversTable controller = loader.getController();
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.setTitle("Přestupky řidičů");
        stage.setScene(scene);
        stage.setOnShown(e -> controller.initialize(dates));


        stage.setOnHidden(e -> {

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

        stage.setOnHidden(e -> {

        });
        stage.showAndWait();
    }

    public void displayWorstAndBestPoliceman(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("I have a great message for you!");

        alert.showAndWait();
    }
}
