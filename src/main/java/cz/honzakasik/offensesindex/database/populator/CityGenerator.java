package cz.honzakasik.offensesindex.database.populator;

import java.util.Random;

/**
 * Created by Jan Kasik on 10.11.15.
 */
abstract class CityGenerator {

    private static String[] cities = new String[]{"Praha", "Brno", "Ostrava", "Zabreh", "Olomouc", "Vyskov", "Zlin"};

    static String getRandomCity(Random random) {
        int i = random.nextInt(cities.length - 1);
        return cities[i];
    }
}
