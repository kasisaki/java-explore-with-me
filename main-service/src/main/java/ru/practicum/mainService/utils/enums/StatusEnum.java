package ru.practicum.mainService.utils.enums;

import ru.practicum.mainService.utils.exceptions.IllegalStatusException;

public enum StatusEnum {
    PENDING,
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
            case "PENDING":
                return PENDING;
            default:
                throw new IllegalStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
