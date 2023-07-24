package ru.practicum.mainService.dto.compilation;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
public class UpdateCompilationRequest {
    private Set<Long> events;
    private Boolean pinned;

    @NotEmpty
    @Size(max = 50)
    private String title;
}
