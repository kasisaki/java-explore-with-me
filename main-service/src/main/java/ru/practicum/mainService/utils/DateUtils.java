package ru.practicum.mainService.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String getDatePatterned() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static String patternDate(LocalDateTime ldt) {
        return ldt.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }
}
