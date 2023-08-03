package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatHitDto;
import ru.practicum.dto.StatResponseDto;
import ru.practicum.server.service.StatService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ru.practicum.utils.Constants.DATE_PATTERN;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatController {

    private final StatService service;

    @GetMapping("/stats")
    public ResponseEntity<List<StatResponseDto>> getStatistics(
            @RequestParam(name = "start") @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime start,
            @RequestParam(name = "end") @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime end,
            @RequestParam(name = "unique", required = false, defaultValue = "false") Boolean unique,
            @RequestParam(name = "uris", required = false) List<String> uris,
            HttpServletRequest request) {
        log.info(
                "GET Statistics request:" +
                        "\n     For Uris:    {}," +
                        "\n     Start Time:  {}," +
                        "\n     End Time:    {}," +
                        "\n     Unique hits: {}," +
                        "\n     from IP:     {}",
                uris, start, end, unique, request.getRemoteAddr());
        return new ResponseEntity<>(service.getStatistics(start, end, unique, uris), HttpStatus.OK);
    }

    @GetMapping("/stats/hits")
    public ResponseEntity<Map<Long, Long>> getViewsOfEvent(@RequestParam(name = "uris") List<String> uris) {
        System.out.println(uris);
        return new ResponseEntity<>(service.getHitsOfEvent(uris), HttpStatus.OK);
    }

    @PostMapping("/hit")
    public ResponseEntity<Integer> hit(@Valid @RequestBody StatHitDto hit,
                                       HttpServletRequest request) {
        log.info(
                "Append Statistics request:" +
                        "\n     Uri:         {}," +
                        "\n     App:         {}," +
                        "\n     IP:          {}," +
                        "\n     access time: {}," +
                        "\n     from IP:     {}",
                hit.getUri(), hit.getApp(), hit.getIp(), hit.getTimestamp(), request.getRemoteAddr());
        service.hit(hit);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
