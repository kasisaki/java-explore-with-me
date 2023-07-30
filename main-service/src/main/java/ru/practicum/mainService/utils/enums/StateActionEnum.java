package ru.practicum.mainService.utils.enums;

import ru.practicum.mainService.utils.exceptions.IllegalStatusException;

public enum StateActionEnum {
    PUBLISH_EVENT,
    REJECT_EVENT,
    SEND_TO_REVIEW,
    CANCEL_REVIEW;

    public static StateActionEnum ofString(String str) {
        switch (str) {
            case "PUBLISH_EVENT":
                return PUBLISH_EVENT;
            case "REJECT_EVENT":
                return REJECT_EVENT;
            case "SEND_TO_REVIEW":
                return SEND_TO_REVIEW;
            case "CANCEL_REVIEW":
                return CANCEL_REVIEW;
            default:
                throw new IllegalStatusException("Unknown state: UNSUPPORTED_STATE_ACTION");
        }
    }
}
