package cz.honzakasik.offensesindex;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Jan Kasik on 4.11.15.
 */
public class DepartmentTableItem {

    private StringProperty name = new SimpleStringProperty();
    private StringProperty location = new SimpleStringProperty();
    private IntegerProperty offenseCount = new SimpleIntegerProperty();
    private IntegerProperty id = new SimpleIntegerProperty();


    public DepartmentTableItem(String name, String location, Integer offenseCount, Integer id) {
        setName(name);
        setLocation(location);
        setOffenseCount(offenseCount);
        setId(id);
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

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
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


    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    @Override
    public String toString() {
        return "DepartmentTableItem{" +
                "name=" + name +
                ", location=" + location +
                ", offenseCount=" + offenseCount +
                ", id=" + id +
                '}';
    }
}
