package ru.practicum.mainService.utils;

import ru.practicum.mainService.utils.exceptions.BadRequestException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.utils.Constants.END_DATE;

public class DateUtils {
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String getDatePatterned() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static String patternDate(LocalDateTime ldt) {
        return ldt.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static void handleDateRange(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.parse(END_DATE, DateTimeFormatter.ofPattern(DATE_PATTERN));
        }
        if (rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Invalid range");
        }
    }
}
