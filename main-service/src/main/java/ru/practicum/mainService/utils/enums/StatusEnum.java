package ru.practicum.mainService.utils.enums;

import ru.practicum.mainService.utils.exceptions.IllegalStatusException;

public enum StatusEnum {
    WAITING,
    CONFIRMED,
    REJECTED,
    CANCELED;

    public static StatusEnum ofString(String str) {
        switch (str) {
            case "REJECTED":
                return REJECTED;
            case "CONFIRMED":
                return CONFIRMED;
            case "CANCELED":
                return CANCELED;
            case "WAITING":
                return WAITING;
            default:
                throw new IllegalStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
