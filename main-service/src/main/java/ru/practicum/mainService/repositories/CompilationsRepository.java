package ru.practicum.mainService.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainService.models.Compilation;

@Repository
public interface CompilationsRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> findAllByPinnedIsTrue(Pageable pageable);
}
