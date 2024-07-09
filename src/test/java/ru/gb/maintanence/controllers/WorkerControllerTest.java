package ru.gb.maintanence.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gb.maintanence.model.Tool;
import ru.gb.maintanence.model.Worker;
import ru.gb.maintanence.repositories.WorkerRepository;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
class WorkerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    WorkerRepository workerRepository;

    @Test
    void findAll() {
        List<Worker> workers = workerRepository.findAll();

        List<Worker> responseBody = webTestClient.get()
                .uri("workers")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<Worker>>() {})
                .returnResult()
                .getResponseBody();
        Assertions.assertEquals(responseBody.size(), workers.size());
        for (Worker worker:responseBody) {
            boolean found = workers.stream()
                    .filter(it -> Objects.equals(worker.getId(), it.getId()))
                    .anyMatch(it -> Objects.equals(worker.getSurname(), it.getSurname()));
            Assertions.assertTrue(found);
        }
    }

    @Test
    void findById() {
        Worker savedWorker = workerRepository.save(new Worker("testSurname", "testName"));

        Worker responseBody = webTestClient.get()
                .uri("workers/" + savedWorker.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Worker.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(responseBody.getId(), savedWorker.getId());
        Assertions.assertEquals(responseBody.getName(), savedWorker.getName());
    }
}