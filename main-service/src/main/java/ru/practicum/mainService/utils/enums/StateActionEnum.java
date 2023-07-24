package ru.practicum.mainService.utils.enums;

import ru.practicum.mainService.utils.exceptions.IllegalStatusException;

public enum StateActionEnum {
    PUBLISH_EVENT,
    REJECT_EVENT;

    public static StateActionEnum ofString(String str) {
        switch (str) {
            case "PUBLISH_EVENT":
                return PUBLISH_EVENT;
            case "REJECT_EVENT":
                return REJECT_EVENT;
            default:
                throw new IllegalStatusException("Unknown state: UNSUPPORTED_STATE_ACTION");
        }
    }
}
