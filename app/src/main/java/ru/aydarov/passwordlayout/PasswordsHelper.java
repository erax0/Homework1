package ru.aydarov.passwordlayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PasswordsHelper {
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%&*()_+-=[]|,./?><";
    private final HashMap<String, String> stringHashMap;

    public PasswordsHelper(String[] russians, String[] latins) {
        if (russians.length != latins.length) {
            throw new IllegalArgumentException();
        }
        stringHashMap = new HashMap<>();
        for (int i = 0; i < russians.length; i++) {
            stringHashMap.put(russians[i], latins[i]);

        }

    }

    public int getQuality(CharSequence password) {
        int letters = 0;
        int digit = 0;
        int symbols = 0;
        int quality = 0;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLetter(password.charAt(i))) {
                ++letters;
                if (letters == 1) ++quality;
                else if (letters == 3) ++quality;
                else if (letters == 6) ++quality;
                else if (letters == 9) ++quality;

            } else if (Character.isDigit(password.charAt(i))) {
                ++digit;
                if (digit == 1) ++quality;
                else if (digit == 2) ++quality;
                else if (digit == 3) ++quality;
            } else {
                ++symbols;
                if (symbols == 1) ++quality;
                else if (symbols == 2) ++quality;
                else if (symbols == 3) ++quality;
            }

        }


        return Math.min(quality, 10);
    }

    public String convert(CharSequence source) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            String sc = String.valueOf(c).toLowerCase();
            if (stringHashMap.containsKey(sc)) {
                String value = stringHashMap.get(sc);
                result.append(Character.isLowerCase(c) ? value : value != null ? value.toUpperCase() : null);
            } else {
                result.append(sc);
            }
        }
        return result.toString();
    }

    public String generate(int length, boolean useUpper, boolean useDigits, boolean useSymbols) {
        StringBuilder password = new StringBuilder();
        List<String> charCategories = new ArrayList<>();
        Random random = new Random(System.nanoTime());
        if (length == 0) {
            return "";
        }
        charCategories.add(LOWER);
        if (useUpper) {
            charCategories.add(UPPER);
        }
        if (useDigits) {
            charCategories.add(DIGITS);
        }
        if (useSymbols) {
            charCategories.add(SYMBOLS);
        }


        for (int i = 0; i < length; i++) {
            String charCategory = charCategories.get(random.nextInt(charCategories.size()));
            int position = random.nextInt(charCategory.length());
            password.append(charCategory.charAt(position));
        }
        return password.toString();
    }
}

