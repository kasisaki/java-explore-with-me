package ru.practicum.mainService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainService.dto.user.NewUserRequest;
import ru.practicum.mainService.dto.user.UserDto;
import ru.practicum.mainService.mappers.UserMapper;
import ru.practicum.mainService.repositories.UserRepository;
import ru.practicum.mainService.utils.exceptions.ElementNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mainService.mappers.UserMapper.newToUser;
import static ru.practicum.mainService.mappers.UserMapper.userToDto;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return userRepository.getAllByIdIn(ids, PageRequest.of(from, size))
                .stream()
                .map(UserMapper::userToDto)
                .collect(Collectors.toList());
    }

    public void delete(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ElementNotFoundException("User " + userId + "not found");
        }
        userRepository.deleteById(userId);
    }

    public UserDto create(NewUserRequest newUserRequest) {
        return userToDto(userRepository.save(newToUser(newUserRequest)));
    }
}
