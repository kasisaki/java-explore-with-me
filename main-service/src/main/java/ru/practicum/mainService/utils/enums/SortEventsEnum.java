package ru.practicum.mainService.utils.enums;

import ru.practicum.mainService.utils.exceptions.IllegalStatusException;

public enum SortEventsEnum {
    VIEWS,
    EVENT_DATE;

    public static SortEventsEnum ofString(String str) {
        switch (str) {
            case "VIEWS":
                return VIEWS;
            case "EVENT_DATE":
                return EVENT_DATE;
            default:
                throw new IllegalStatusException("Unknown state: UNSUPPORTED_SORT_TYPE");
        }
    }
}
