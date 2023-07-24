package ru.practicum.mainService.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainService.dto.user.UserDto;
import ru.practicum.mainService.models.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<UserDto> getAllByIdIn(List<Long> ids, PageRequest of);
}
