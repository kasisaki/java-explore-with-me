package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class StatHitDto {

    @NotEmpty
    private String uri;

    @NotEmpty
    private String ip;

    @NotEmpty
    private String app;

    @NotNull
    private LocalDateTime timestamp;
}