package ru.practicum.mainService.mappers;

import lombok.RequiredArgsConstructor;
import ru.practicum.mainService.dto.compilation.CompilationDto;
import ru.practicum.mainService.dto.compilation.NewCompilationDto;
import ru.practicum.mainService.models.Compilation;

@RequiredArgsConstructor
public class CompilationMapper {

    public static CompilationDto compToDto(Compilation compilation) {
        if (compilation == null) {
            return null;
        }

        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .build();
    }

    public static CompilationDto newToCompDto(NewCompilationDto newComp) {
        if (newComp == null) {
            return null;
        }
        return CompilationDto.builder()
                .title(newComp.getTitle())
                .pinned(newComp.isPinned())
                .build();
    }

    public static Compilation dtoToComp(CompilationDto dto) {
        if (dto == null) {
            return null;
        }
        return Compilation.builder()
                .id(dto.getId())
                .events(dto.getEvents())
                .title(dto.getTitle())
                .pinned(dto.isPinned()).build();
    }

    public static Compilation newToComp(NewCompilationDto dto) {
        if (dto == null) {
            return null;
        }
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.isPinned()).build();
    }
}
