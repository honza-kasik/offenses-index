package cz.honzakasik.offensesindex.drivers;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Created by Jan Kasik on 18.11.15.
 */
public class Driver {

    private StringProperty name = new SimpleStringProperty();
    private StringProperty surname = new SimpleStringProperty();
    private StringProperty city = new SimpleStringProperty();
    private StringProperty street = new SimpleStringProperty();
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty gender = new SimpleStringProperty();
    private ObjectProperty<LocalDate> dateOfBirth = new SimpleObjectProperty<>();

    public Driver(StringProperty name, StringProperty surname, StringProperty city, StringProperty street,
                  IntegerProperty id, StringProperty gender, ObjectProperty<LocalDate> dateOfBirth) {
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.street = street;
        this.id = id;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    public Driver(String name, String surname, String city, String street, int id, String gender,
                  LocalDate dateOfBirth) {
        setName(name);
        setSurname(surname);
        setCity(city);
        setStreet(street);
        setGender(gender);
        setDateOfBirth(dateOfBirth);
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

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getStreet() {
        return street.get();
    }

    public StringProperty streetProperty() {
        return street;
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth.get();
    }

    public ObjectProperty<LocalDate> dateOfBirthProperty() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }
}
