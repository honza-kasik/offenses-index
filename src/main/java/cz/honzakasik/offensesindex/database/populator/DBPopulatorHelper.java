package cz.honzakasik.offensesindex.database.populator;

import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import java.util.Random;

import static cz.honzakasik.offensesindex.database.DatabaseNames.*;

/**
 * Created by Jan Kasik on 8.11.15.
 */
abstract class DBPopulatorHelper {

    private static Random rnd = new Random();

    static String createInsertUniqueDriverQuery(DbTable table, int id) {
        return new InsertQuery(table)
                .addCustomColumn(ID, id)
                .addCustomColumn(NAME, NameGenerator.randomIdentifier())
                .addCustomColumn(SURNAME, NameGenerator.randomIdentifier())
                .addCustomColumn(DATE_OF_BIRTH, DateGenerator.randomDate())
                .addCustomColumn(CITY, CityGenerator.getRandomCity(rnd))
                .addCustomColumn(SEX, rnd.nextInt(1) == 1 ? "muz" : "zena")
                .validate().toString();
    }

    static String createInsertUniquePolicemanQuery(DbTable table, int id, int departmentCount) {
        return new InsertQuery(table)
                .addCustomColumn(ID, id)
                .addCustomColumn(NAME, NameGenerator.randomIdentifier())
                .addCustomColumn(SURNAME, NameGenerator.randomIdentifier())
                .addCustomColumn(NUMBER, rnd.nextInt(8999) + 1000) //4 digit number
                .addCustomColumn(DEPARTMENT_ID, rnd.nextInt(departmentCount))
                .validate().toString();
    }

    static String createInsertOffenseQuery(DbTable table, int id, int pointCount, String name) {
        return new InsertQuery(table)
                .addCustomColumn(ID, id)
                .addCustomColumn(POINT_COUNT, pointCount)
                .addCustomColumn(NAME, name)
                .validate().toString();
    }

    static String createInsertDepartmentQuery(DbTable table, int id, String name, String city) {
        return new InsertQuery(table)
                .addCustomColumn(ID, id)
                .addCustomColumn(NAME, name)
                .addCustomColumn(CITY, city)
                .validate().toString();
    }

    static String createInsertEventQuery(DbTable table, int id, int policemenCount, int driversCount, int offenseCount) {
        return new InsertQuery(table)
                .addCustomColumn(ID, id)
                .addCustomColumn(POLICEMAN_ID, rnd.nextInt(policemenCount))
                .addCustomColumn(DRIVER_ID, rnd.nextInt(driversCount))
                .addCustomColumn(OFFENSE_ID, rnd.nextInt(offenseCount))
                .addCustomColumn(DATE, DateGenerator.randomDate())
                .addCustomColumn(DESCRIPTION, NameGenerator.randomIdentifier())
                .validate().toString();
    }


}
