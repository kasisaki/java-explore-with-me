package ru.practicum.mainService.utils.enums;

import ru.practicum.mainService.utils.exceptions.IllegalStatusException;

public enum StateActionUserEnum {
    SEND_TO_REVIEW,
    CANCEL_REVIEW;

    public static StateActionUserEnum ofString(String str) {
        switch (str) {
            case "SEND_TO_REVIEW":
                return SEND_TO_REVIEW;
            case "CANCEL_REVIEW":
                return CANCEL_REVIEW;
            default:
                throw new IllegalStatusException("Unknown state: UNSUPPORTED_STATE_ACTION");
        }
    }
}
