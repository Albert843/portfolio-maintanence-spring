package ru.gb.maintanence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.maintanence.model.Job;
import ru.gb.maintanence.model.Worker;

public interface JobRepository extends JpaRepository<Job, Long> {
    long countByWorkerAndReturnedAtIsNull(Worker worker);
}
