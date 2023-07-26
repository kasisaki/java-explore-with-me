package ru.practicum.mainService.utils.enums;

import ru.practicum.mainService.utils.exceptions.IllegalStatusException;

public enum StatusEnum {
    PENDING,
    PUBLISHED,
    REJECTED,
    CANCELED;

    public static StatusEnum ofString(String str) {
        switch (str) {
            case "REJECTED":
                return REJECTED;
            case "PUBLISHED":
                return PUBLISHED;
            case "CANCELED":
                return CANCELED;
            case "PENDING":
                return PENDING;
            default:
                throw new IllegalStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
