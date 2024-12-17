package com.isep.certification.commons.utils;

import java.util.Random;

public class RandomStringGenerator {
    public static String generateNumber() {
        Random rnd = new Random();
        int number = rnd.nextInt(9999999);
        return String.format("%06d", number);
    }

    public static String generateNumbersAndLetters(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";
        StringBuilder s = new StringBuilder(n);
        int y;
        for (y = 0; y < n; y++) {
            int index = (int) (AlphaNumericString.length()
                    * Math.random());
            s.append(AlphaNumericString
                    .charAt(index));
        }
        return s.toString();
    }
}
