package cz.honzakasik.offensesindex.policemen;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Jan Kasik on 8.11.15.
 */
public class PolicemanTableItem {

    private StringProperty name = new SimpleStringProperty();
    private StringProperty surname = new SimpleStringProperty();
    private IntegerProperty number = new SimpleIntegerProperty();
    private IntegerProperty offenseCount = new SimpleIntegerProperty();

    public PolicemanTableItem(String name, String surname, int number, int offenseCount) {
        setName(name);
        setSurname(surname);
        setNumber(number);
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

    public String getSurname() {
        return surname.get();
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public int getNumber() {
        return number.get();
    }

    public IntegerProperty numberProperty() {
        return number;
    }

    public void setNumber(int number) {
        this.number.set(number);
    }

    public int getOffenseCount() {
        return offenseCount.get();
    }

    public IntegerProperty offenseCountProperty() {
        return offenseCount;
    }

    public void setOffenseCount(int offenseCount) {
        this.offenseCount.set(offenseCount);
    }
}
