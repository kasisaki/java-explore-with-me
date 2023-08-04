package ru.practicum.mainService.dto.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateEventAdminRequest extends UpdateEventUserRequest {
}
