package cz.honzakasik.offensesindex.offenses;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Jan Kasik on 15.11.15.
 */
public class OffensesTableItem {

    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty offensesCount = new SimpleIntegerProperty();


    public OffensesTableItem(String name, int offenseCount) {
        setName(name);
        setOffenseCount(offenseCount);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getOffensesCount() {
        return offensesCount.get();
    }

    public IntegerProperty offensesCountProperty() {
        return offensesCount;
    }

    public void setOffenseCount(int offenseCount) {
        this.offensesCount.set(offenseCount);
    }
}
