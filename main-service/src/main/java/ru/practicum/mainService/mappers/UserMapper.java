package ru.practicum.mainService.mappers;

import ru.practicum.mainService.dto.user.NewUserRequest;
import ru.practicum.mainService.dto.user.UserDto;
import ru.practicum.mainService.dto.user.UserShortDto;
import ru.practicum.mainService.models.User;

public class UserMapper {

    private UserMapper() {
        throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

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

    public static UserShortDto userToShortDto(User user) {
        if (user == null) {
            return null;
        }
        return UserShortDto.builder()
                .name(user.getName())
                .id(user.getId())
                .build();
    }

    public static User userShortDtoToUser(UserShortDto userShortDto) {
        if (userShortDto == null) {
            return null;
        }
        return User.builder()
                .name(userShortDto.getName())
                .id(userShortDto.getId())
                .build();
    }


}
