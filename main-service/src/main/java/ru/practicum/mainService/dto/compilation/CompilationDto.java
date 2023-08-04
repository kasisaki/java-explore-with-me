package ru.practicum.mainService.dto.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.mainService.models.Event;

import java.util.Set;

@Data
@Builder
public class CompilationDto {
    private Set<Event> events;
    private Long id;
    private boolean pinned;
    private String title;
}
