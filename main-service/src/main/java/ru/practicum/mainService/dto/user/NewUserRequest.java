package ru.practicum.mainService.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class NewUserRequest {

    @NotEmpty
    @NotBlank
    @Size(min = 2, max = 250, message = "Wrong User name length")
    private String name;

    @Email
    @NotEmpty
    @Size(min = 6, max = 254, message = "Wrong user email length")
    private String email;
}
