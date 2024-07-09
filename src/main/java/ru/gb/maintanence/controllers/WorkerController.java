package ru.gb.maintanence.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.gb.maintanence.model.Worker;
import ru.gb.maintanence.services.WorkerService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("workers")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    /**
     * Метод поиска всех работников по endpoint "workers"
     * @return статус ОК и список работников
     */
    @GetMapping()
    public ResponseEntity<List<Worker>> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(workerService.findAll());
    }

    /**
     * Метод поиска работника по id
     * @param id id работника
     * @return статус ОК и работника
     */
    @GetMapping("{id}")
    public ResponseEntity<Worker> findById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(workerService.findById(id));
    }

    /**
     * Метод добавления работника
     * @param worker работник
     * @return статус Created и работника
     */
    @PostMapping()
    public ResponseEntity<Worker> createWorker(@RequestBody Worker worker) {
        Worker createdWorker = workerService.createWorker(worker);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("id")
                .buildAndExpand(createdWorker.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdWorker);
    }

    /**
     * Метод обновления работника по id
     * @param id  id работника
     * @param worker работник
     * @return статус ОК и интрумент
     */
    @PutMapping("{id}")
    public ResponseEntity<Worker> updateWorker(@PathVariable long id, @RequestBody Worker worker) {
        return ResponseEntity.status(HttpStatus.OK).body(workerService.updateWorker(id, worker));
    }

    /**
     * Метод удаления работника по id
     * @param id id работника
     * @return статус NoContent
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable long id) {
        workerService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
