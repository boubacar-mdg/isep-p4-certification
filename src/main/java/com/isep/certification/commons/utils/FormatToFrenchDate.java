package com.isep.certification.commons.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatToFrenchDate {
    public static String format(LocalDate date) {
        DateTimeFormatter frenchDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.FRENCH);
        return date.format(frenchDateFormatter);
    }

    public static String format(LocalDateTime date) {
        DateTimeFormatter frenchDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'a' HH'h'mm", Locale.FRENCH);
        return date.format(frenchDateFormatter);
    }
}