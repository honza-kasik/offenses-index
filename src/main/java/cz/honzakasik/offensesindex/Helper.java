package cz.honzakasik.offensesindex;


/**
 * Created by Jan Kasik on 4.11.15.
 */
public abstract class Helper {

    public static boolean isYear(String str)
    {
        return str.matches("[1-9]\\d{3}");  //match a number starting with 1-9
    }

    public static boolean isMonth(String string) {
        return string.matches("(0?[1-9]|1[012])");
    }

}
