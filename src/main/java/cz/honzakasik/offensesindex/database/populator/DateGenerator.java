package cz.honzakasik.offensesindex.database.populator;

import java.time.LocalDate;

/**
 * Created by Jan Kasik on 8.11.15.
 */
abstract class DateGenerator {

    static LocalDate randomDate() {
        return randomDate(1900, 2015);
    }

    static LocalDate randomDate(int fromYear, int toYear) {
        LocalDate date = LocalDate.ofYearDay(randBetween(fromYear, toYear), 1);

        return  date.withDayOfYear(randBetween(1, date.lengthOfYear()));
    }

    private static int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }


}
