package cz.honzakasik.offensesindex.database.populator;

import cz.honzakasik.offensesindex.database.DBManager;

import java.util.Random;

/**
 * Created by Jan Kasik on 10.11.15.
 */
public class DBPopulator {

    private DBManager dbManager;
    private Random random = new Random();
    private final int POLICEMEN_COUNT = 10;
    private final int DRIVERS_COUNT = 20;
    private final int EVENTS_COUNT = 50;
    private final int DEPARTMENTS_COUNT = 5;
    private final int OFFENSES_COUNT = 5;

    public DBPopulator(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    private void populatePolicemenTable() {
        for (int i = 0; i < POLICEMEN_COUNT; i++)
            dbManager.executeSQL(
                    DBPopulatorHelper.createInsertUniquePolicemanQuery(
                            dbManager.getPolicemenTable(),
                            i,
                            DEPARTMENTS_COUNT));
    }

    private void populateDriversTable() {
        for (int i = 0; i < DRIVERS_COUNT; i++)
            dbManager.executeSQL(DBPopulatorHelper.createInsertUniqueDriverQuery(dbManager.getDriversTable(), i));
    }

    private void populateDepartmentsTable() {
        for (int i = 0; i < DEPARTMENTS_COUNT; i++)
            dbManager.executeSQL(
                    DBPopulatorHelper.createInsertDepartmentQuery(
                            dbManager.getDepartmentsTable(),
                            i,
                            NameGenerator.randomIdentifier(),
                            CityGenerator.getRandomCity(random)));
    }

    private void populateEventsTable() {
        for (int i = 0; i < EVENTS_COUNT; i++)
            dbManager.executeSQL(DBPopulatorHelper.createInsertEventQuery(
                    dbManager.getEventsTable(),
                    i,
                    POLICEMEN_COUNT,
                    DRIVERS_COUNT,
                    OFFENSES_COUNT));
    }

    private void populateOffensesTable() {
        for (int i = 0; i < OFFENSES_COUNT; i++)
            dbManager.executeSQL(DBPopulatorHelper.createInsertOffenseQuery(
                    dbManager.getOffensesTable(),
                    i,
                    random.nextInt(11) + 1,
                    NameGenerator.randomIdentifier()));
    }

    public void populateDatabase() {
        populateDepartmentsTable();
        populateDriversTable();
        populateEventsTable();
        populateOffensesTable();
        populatePolicemenTable();
    }

}
