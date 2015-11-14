package cz.honzakasik.offensesindex;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jan Kasik on 14.11.15.
 */
public class OffensesTabController implements Initializable {

    @FXML TableView offensesTable;
    @FXML TextField monthFrom;
    @FXML TextField monthTo;
    @FXML ChoiceBox pointCount;
    @FXML Button showOffensesButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void showOffensesWithinMonthsAction(ActionEvent actionEvent) {
    }
}
