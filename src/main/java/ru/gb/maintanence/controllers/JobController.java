package ru.gb.maintanence.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.gb.maintanence.model.Job;
import ru.gb.maintanence.services.JobService;

import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("jobs")
public class JobController {

    private final JobService jobService;

    /**
     * Метод поиска всех работ по endpoint "jobs"
     * @return статус ОК и список работ
     */
    @GetMapping
    public ResponseEntity<List<Job>> findAll() {
        return ResponseEntity.ok().body(jobService.findAll());
    }

    /**
     * Метод поиска работы по id по endpoint "jobs/{id}"
     * @param id id работы
     * @return статус ОК и работу
     */
    @GetMapping("{id}")
    public ResponseEntity<Job> findById(@PathVariable long id) {
        return ResponseEntity.ok().body(jobService.findById(id));
    }

    /**
     * Метод создания работы
     * @param job работа
     * @return статус Created и работу
     */
    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job createdJob = jobService.saveJob(job);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdJob.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdJob);
    }

    /**
     * Метод обновления инструмента в работе по id
     * @param id id работы
     * @return статус ОК и id инструмента
     */
    @PutMapping("{id}")
    public ResponseEntity<Job> updateTool(@PathVariable long id) {
        return ResponseEntity.ok().body(jobService.returnTool(id));
    }

    /**
     * Метод удаления работы
     * @param id id работы
     * @return статус NoContent
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable long id) {
        jobService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
