package ru.practicum.mainService.controller.authorised.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.dto.user.NewUserRequest;
import ru.practicum.mainService.dto.user.UserDto;
import ru.practicum.mainService.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService service;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        return new ResponseEntity<>(service.create(newUserRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam final List<Long> ids,
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(service.getUsers(ids, from, size), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        service.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
