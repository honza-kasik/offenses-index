package cz.honzakasik.offensesindex;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Jan Kasik on 28.10.15.
 */
public class DriverTableItem {

    private StringProperty name = new SimpleStringProperty();
    private StringProperty surname = new SimpleStringProperty();
    private IntegerProperty pointCount = new SimpleIntegerProperty();
    private StringProperty address = new SimpleStringProperty();
    private IntegerProperty offenseCount = new SimpleIntegerProperty();
    private IntegerProperty id = new SimpleIntegerProperty();

    public DriverTableItem(StringProperty name, StringProperty surname, IntegerProperty pointCount, IntegerProperty offensesCount, StringProperty city) {
        this.name = name;
        this.surname = surname;
        this.pointCount = pointCount;
        this.offenseCount = offensesCount;
        this.address = city;
    }

    public DriverTableItem(String name, String surname, Integer pointCount, Integer offenseCount, String city, Integer id) {
        setName(name);
        setSurname(surname);
        setPointCount(pointCount);
        setOffenseCount(offenseCount);
        setAddress(city);
        setId(id);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getOffenseCount() {
        return offenseCount.get();
    }

    public IntegerProperty offenseCountProperty() {
        return offenseCount;
    }

    public void setOffenseCount(int offensesCount) {
        this.offenseCount.set(offensesCount);
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

    public int getPointCount() {
        return pointCount.get();
    }

    public IntegerProperty pointCountProperty() {
        return pointCount;
    }

    public void setPointCount(int pointCount) {
        this.pointCount.set(pointCount);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }
}
