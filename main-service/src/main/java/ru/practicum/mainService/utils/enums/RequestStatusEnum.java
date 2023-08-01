package ru.practicum.mainService.utils.enums;

import ru.practicum.mainService.utils.exceptions.IllegalStatusException;

public enum RequestStatusEnum {
    PENDING,
    ACCEPTED,
    REJECTED,
    CANCELED;

    public static RequestStatusEnum ofString(String str) {
        switch (str) {
            case "REJECTED":
                return REJECTED;
            case "ACCEPTED":
                return ACCEPTED;
            case "CANCELED":
                return CANCELED;
            case "PENDING":
                return PENDING;
            default:
                throw new IllegalStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
