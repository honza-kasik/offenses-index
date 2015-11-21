package cz.honzakasik.offensesindex.drivers;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Created by Jan Kasik on 28.10.15.
 */
public class DriverTableItem extends Driver {

    private IntegerProperty offenseCount = new SimpleIntegerProperty();
    private IntegerProperty pointCount = new SimpleIntegerProperty();

    public DriverTableItem(StringProperty name, StringProperty surname, StringProperty city, StringProperty street,
                           IntegerProperty id, StringProperty gender, ObjectProperty<LocalDate> dateOfBirth,
                           IntegerProperty offenseCount, IntegerProperty pointCount) {
        super(name, surname, city, street, id, gender, dateOfBirth);
        this.offenseCount = offenseCount;
        this.pointCount = pointCount;

    }

    public DriverTableItem(String name, String surname, String city, String street, int id, String gender,
                           LocalDate dateOfBirth, int pointCount, int offenseCount) {
        super(name, surname, city, street, id, gender, dateOfBirth);
        setPointCount(pointCount);
        setOffenseCount(offenseCount);
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

    public int getPointCount() {
        return pointCount.get();
    }

    public IntegerProperty pointCountProperty() {
        return pointCount;
    }

    public void setPointCount(int pointCount) {
        this.pointCount.set(pointCount);
    }
}
