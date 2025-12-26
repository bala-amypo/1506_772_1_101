/*package com.example.demo.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    public static long daysBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }
}*/
package com.example.demo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for date and time operations
 */
public class DateUtil {
    
    // Private constructor to prevent instantiation
    private DateUtil() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }
    
    // Common date patterns
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DISPLAY_DATE_PATTERN = "dd/MM/yyyy";
    public static final String DISPLAY_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm";
    
    // Formatters
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    private static final DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(ISO_DATE_TIME_PATTERN);
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern(DISPLAY_DATE_PATTERN);
    private static final DateTimeFormatter DISPLAY_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DISPLAY_DATE_TIME_PATTERN);
    
    /**
     * Format LocalDate to string using default pattern (yyyy-MM-dd)
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }
    
    /**
     * Format LocalDateTime to string using default pattern (yyyy-MM-dd HH:mm:ss)
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }
    
    /**
     * Format LocalDateTime to ISO string (yyyy-MM-dd'T'HH:mm:ss)
     */
    public static String formatIsoDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(ISO_DATE_TIME_FORMATTER);
    }
    
    /**
     * Format LocalDate to display string (dd/MM/yyyy)
     */
    public static String formatDisplayDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DISPLAY_DATE_FORMATTER);
    }
    
    /**
     * Format LocalDateTime to display string (dd/MM/yyyy HH:mm)
     */
    public static String formatDisplayDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DISPLAY_DATE_TIME_FORMATTER);
    }
    
    /**
     * Parse string to LocalDate using default pattern (yyyy-MM-dd)
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateString.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected: " + DATE_PATTERN, e);
        }
    }
    
    /**
     * Parse string to LocalDateTime using default pattern (yyyy-MM-dd HH:mm:ss)
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeString.trim(), DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid datetime format. Expected: " + DATE_TIME_PATTERN, e);
        }
    }
    
    /**
     * Check if a date is in the future
     */
    public static boolean isFutureDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(LocalDate.now());
    }
    
    /**
     * Check if a date is in the past
     */
    public static boolean isPastDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isBefore(LocalDate.now());
    }
    
    /**
     * Check if a date is today
     */
    public static boolean isToday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isEqual(LocalDate.now());
    }
    
    /**
     * Check if a datetime is in the future
     */
    public static boolean isFutureDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.isAfter(LocalDateTime.now());
    }
    
    /**
     * Check if a datetime is in the past
     */
    public static boolean isPastDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.isBefore(LocalDateTime.now());
    }
    
    /**
     * Get date X days ago from today
     */
    public static LocalDate getDateDaysAgo(int days) {
        return LocalDate.now().minusDays(days);
    }
    
    /**
     * Get date X days from today
     */
    public static LocalDate getDateDaysFromNow(int days) {
        return LocalDate.now().plusDays(days);
    }
    
    /**
     * Calculate number of days between two dates
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
    
    /**
     * Calculate number of days between two datetimes
     */
    public static long daysBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDateTime, endDateTime);
    }
    
    /**
     * Calculate number of business days between two dates (excluding weekends)
     */
    public static long businessDaysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return 0;
        }
        
        long businessDays = 0;
        LocalDate date = startDate;
        
        while (!date.isAfter(endDate)) {
            if (!isWeekend(date)) {
                businessDays++;
            }
            date = date.plusDays(1);
        }
        
        return businessDays;
    }
    
    /**
     * Check if a date is weekend (Saturday or Sunday)
     */
    public static boolean isWeekend(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.getDayOfWeek().getValue() >= 6; // Saturday = 6, Sunday = 7
    }
    
    /**
     * Get start of month for a given date
     */
    public static LocalDate getStartOfMonth(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return date.withDayOfMonth(1);
    }
    
    /**
     * Get end of month for a given date
     */
    public static LocalDate getEndOfMonth(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return date.withDayOfMonth(date.lengthOfMonth());
    }
    
    /**
     * Get start of year for a given date
     */
    public static LocalDate getStartOfYear(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return date.withDayOfYear(1);
    }
    
    /**
     * Get end of year for a given date
     */
    public static LocalDate getEndOfYear(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return date.withDayOfYear(date.lengthOfYear());
    }
    
    /**
     * Get current date
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }
    
    /**
     * Get current datetime
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
    
    /**
     * Get age in years from birth date
     */
    public static int getAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }
    
    /**
     * Check if date is valid (not null and not in the future for consumption logs)
     */
    public static boolean isValidConsumptionDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.isAfter(LocalDate.now());
    }
    
    /**
     * Validate date string format
     */
    public static boolean isValidDateFormat(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return false;
        }
        try {
            LocalDate.parse(dateString.trim(), DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    /**
     * Get first day of current month
     */
    public static LocalDate getFirstDayOfCurrentMonth() {
        return LocalDate.now().withDayOfMonth(1);
    }
    
    /**
     * Get last day of current month
     */
    public static LocalDate getLastDayOfCurrentMonth() {
        return LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
    }
    
    /**
     * Get first day of previous month
     */
    public static LocalDate getFirstDayOfPreviousMonth() {
        return LocalDate.now().minusMonths(1).withDayOfMonth(1);
    }
    
    /**
     * Get last day of previous month
     */
    public static LocalDate getLastDayOfPreviousMonth() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        return lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());
    }
    
    /**
     * Get start of day for a given datetime
     */
    public static LocalDateTime getStartOfDay(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return date.atStartOfDay();
    }
    
    /**
     * Get end of day for a given datetime
     */
    public static LocalDateTime getEndOfDay(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return date.atTime(23, 59, 59);
    }
    
    /**
     * Check if a date is within a date range (inclusive)
     */
    public static boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if (date == null || startDate == null || endDate == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
    
    /**
     * Calculate working days until reorder
     */
    public static int calculateWorkingDaysUntilReorder(int totalDays, LocalDate startDate) {
        if (totalDays <= 0 || startDate == null) {
            return 0;
        }
        
        LocalDate currentDate = startDate;
        int workingDays = 0;
        int daysCounted = 0;
        
        while (daysCounted < totalDays) {
            if (!isWeekend(currentDate)) {
                workingDays++;
            }
            currentDate = currentDate.plusDays(1);
            daysCounted++;
        }
        
        return workingDays;
    }
}