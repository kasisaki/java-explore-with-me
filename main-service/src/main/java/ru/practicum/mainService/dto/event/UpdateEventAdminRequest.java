package ru.practicum.mainService.dto.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.mainService.utils.enums.StateActionEnum;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateEventAdminRequest extends UpdateEventUserRequest {
    private StateActionEnum stateAction;
}
