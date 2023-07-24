package ru.practicum.mainService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainService.dto.compilation.CompilationDto;
import ru.practicum.mainService.dto.compilation.NewCompilationDto;
import ru.practicum.mainService.mappers.CompilationMapper;
import ru.practicum.mainService.models.Event;
import ru.practicum.mainService.repositories.CompilationsRepository;
import ru.practicum.mainService.repositories.EventRepository;
import ru.practicum.mainService.utils.exceptions.ElementNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.mainService.mappers.CompilationMapper.compToDto;
import static ru.practicum.mainService.mappers.CompilationMapper.newToComp;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationsRepository repository;
    private final EventRepository eventRepository;

    public List<CompilationDto> getCompilations(Integer from, Integer size, Boolean pinned) {
        if (pinned) {
            return repository.findAllByPinnedIsTrue(PageRequest.of(from, size))
                    .stream()
                    .map(CompilationMapper::compToDto)
                    .collect(Collectors.toList());
        }
        return repository.findAll(PageRequest.of(from, size))
                .stream()
                .map(CompilationMapper::compToDto)
                .collect(Collectors.toList());
    }

    public CompilationDto getCompilation(Long compId) {
        return compToDto(repository.findById(compId).orElseThrow(
                () -> new ElementNotFoundException("Could not find compilation with id " + compId)));
    }

    @Transactional
    public CompilationDto postCompilation(NewCompilationDto newCompilation) {

        CompilationDto dto = compToDto(repository.save(newToComp(newCompilation)));

        setEvents(newCompilation.getEvents(), dto);
        return dto;
    }

    private void setEvents(Set<Long> ids, CompilationDto dto) {
        Set<Event> events = eventRepository.findAllByIdIn(ids);
        dto.setEvents(events);
    }

    public CompilationDto update(Long compId) {
        return null;
    }

    public void delete(Long compId) {
        repository.deleteById(compId);
    }
}
