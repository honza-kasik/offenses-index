package cz.honzakasik.offensesindex.database;

import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import java.util.Random;

import static cz.honzakasik.offensesindex.database.DatabaseNames.*;

/**
 * Created by Jan Kasik on 8.11.15.
 */
class DBPopulator {

    private Random rnd = new Random();

    private String[] cities = new String[]{"Praha", "Brno", "Ostrava", "Zabreh", "Olomouc", "Vyskov", "Zlin"};

    String createInsertUniqueDriverQuery(DbTable table) {
        return new InsertQuery(table)
                .addCustomColumn(NAME, NameGenerator.randomIdentifier())
                .addCustomColumn(SURNAME, NameGenerator.randomIdentifier())
                .addCustomColumn(DATE_OF_BIRTH, DateGenerator.randomDate())
                .addCustomColumn(CITY, cities[rnd.nextInt(cities.length - 1)])
                .addCustomColumn(SEX, rnd.nextInt(1) == 1 ? "muz" : "zena")
                .validate().toString();
    }

    String createInsertUniquePolicemanQuery(DbTable table, int departmentCount) {
        return new InsertQuery(table)
                .addCustomColumn(NAME, NameGenerator.randomIdentifier())
                .addCustomColumn(SURNAME, NameGenerator.randomIdentifier())
                .addCustomColumn(NUMBER, rnd.nextInt(8999) + 1000) //4 digit number
                .addCustomColumn(DEPARTMENT_ID, rnd.nextInt(departmentCount))
                .validate().toString();
    }

    String createInsertOffenseQuery(DbTable table, int pointCount, String name) {
        return new InsertQuery(table)
                .addCustomColumn(POINT_COUNT, pointCount)
                .addCustomColumn(NAME, name)
                .validate().toString();
    }

    String createInsertDepartmentQuery(DbTable table, String name, String city) {
        return new InsertQuery(table)
                .addCustomColumn(NAME, name)
                .addCustomColumn(CITY, city)
                .validate().toString();
    }

    String createInsertEventQuery(DbTable table, int policemenCount, int driversCount, int offenseCount) {
        return new InsertQuery(table)
                .addCustomColumn(POLICEMAN_ID, rnd.nextInt(policemenCount))
                .addCustomColumn(DRIVER_ID, rnd.nextInt(driversCount))
                .addCustomColumn(OFFENSE_ID, rnd.nextInt(offenseCount))
                .addCustomColumn(DATE, DateGenerator.randomDate())
                .addCustomColumn(DESCRIPTION, NameGenerator.randomIdentifier())
                .validate().toString();
    }


}
