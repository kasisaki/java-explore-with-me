package ru.practicum.mainService.controller.unauthorised;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.dto.compilation.CompilationDto;
import ru.practicum.mainService.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class OpenCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(
            @RequestParam(required = false, defaultValue = "false") Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        return new ResponseEntity<>(compilationService.getCompilations(from, size, pinned), HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable @Positive Long compId) {
        return new ResponseEntity<>(compilationService.getCompilation(compId), HttpStatus.OK);
    }
}
