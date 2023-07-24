package ru.practicum.mainService.mappers;

import ru.practicum.mainService.dto.user.NewUserRequest;
import ru.practicum.mainService.dto.user.UserDto;
import ru.practicum.mainService.models.User;

public class UserMapper {
    public static User newToUser(NewUserRequest newUserRequest) {
        if (newUserRequest == null) {
            return null;
        }
        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail()).build();
    }

    public static UserDto userToDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
