package ru.gb.maintanence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.maintanence.model.Worker;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
}
