package com.example.demo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtil {

    private DateUtil() {
    }

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static boolean isFuture(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }
}
