package ru.practicum.mainService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainService.models.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}
