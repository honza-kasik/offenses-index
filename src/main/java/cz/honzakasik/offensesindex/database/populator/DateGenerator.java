package cz.honzakasik.offensesindex.database.populator;

import java.time.LocalDate;

/**
 * Created by Jan Kasik on 8.11.15.
 */
abstract class DateGenerator {

    static LocalDate randomDate() {

        int year = randBetween(1900, 2010);

        int dayOfYear = randBetween(1, LocalDate.ofYearDay(year, 5).lengthOfYear());

        return  LocalDate.ofYearDay(year, dayOfYear);
    }

    private static int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }


}
