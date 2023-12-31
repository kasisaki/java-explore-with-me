package ru.practicum.mainService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainService.models.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    boolean existsByLatAndLon(float lat, float lon);

    Location findByLatAndLon(float lat, float lon);
}
