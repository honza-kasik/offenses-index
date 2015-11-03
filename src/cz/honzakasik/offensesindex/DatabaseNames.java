package cz.honzakasik.offensesindex;

/**
 * Created by Jan Kasik on 1.11.15.
 */
public abstract class DatabaseNames {

    public static final String NAME = "jmeno";
    public static final String SURNAME = "prijmeni";
    public static final String POINT_COUNT = "pocet_bodu";
    public static final String OFFENSES_COUNT = "pocet_prestupku";
    public static final String CITY = "mesto";
    public static final String DRIVER = "ridic";
    public static final String DRIVERS = "ridici";
    public static final String OFFENSE = "prestupek";
    public static final String OFFENSES = "prestupky";
    public static final String DATE = "datum";
    public static final String EVENT = "udalost";
    public static final String EVENTS = "udalosti";
    public static final String SEX = "pohlavi";
    public static final String DATE_OF_BIRTH = "datum_narozeni";
    public static final String ID = "ID";
    public static final String DRIVER_ID = ID + "_" + DRIVER;
    public static final String OFFENSE_ID = ID + "_" + OFFENSE;
    public static final String POLICEMAN = "policista";
    public static final String POLICEMEN = "policiste";
    public static final String NUMBER = "cislo";
    public static final String DEPARTMENT = "oddeleni";
    public static final String DEPARTMENTS = "oddeleni";
    public static final String DEPARTMENT_ID = ID + "_" + DEPARTMENT;
    public static final String POLICEMAN_ID = ID + "_" + POLICEMAN;
    public static final String DESCRIPTION = "popis";
}
