package ru.gb.maintanence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.maintanence.model.Tool;

public interface ToolRepository extends JpaRepository<Tool, Long> {
}
