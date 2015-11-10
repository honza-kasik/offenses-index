package cz.honzakasik.offensesindex.database.populator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * http://stackoverflow.com/a/5025666/4402950
 */
abstract class NameGenerator {

    private static final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

    private static final Random rand = new Random();

    private static final Set<String> identifiers = new HashSet<>();

    static String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while (builder.toString().length() == 0) {
            int length = rand.nextInt(5) + 5;
            for (int i = 0; i < length; i++)
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            if (identifiers.contains(builder.toString()))
                builder = new StringBuilder();
        }
        return builder.toString();
    }


}
