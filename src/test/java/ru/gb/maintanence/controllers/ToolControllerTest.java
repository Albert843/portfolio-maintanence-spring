package ru.gb.maintanence.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gb.maintanence.model.Tool;
import ru.gb.maintanence.repositories.ToolRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
class ToolControllerTest {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    ToolRepository toolRepository;

    @Test
    void findAll() {
        List<Tool> tools = toolRepository.findAll();

        List<Tool> responseBody = webTestClient.get()
                .uri("tools")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<Tool>>() {
                })
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals(responseBody.size(), tools.size());
        for (Tool tool:responseBody) {
            boolean found = tools.stream()
                    .filter(it -> Objects.equals(tool.getId(), it.getId()))
                    .anyMatch(it -> Objects.equals(tool.getName(), it.getName()));
            Assertions.assertTrue(found);
        }
    }

    @Test
    void findById() {
        Tool savedTool = toolRepository.save(new Tool("testTool"));

        Tool responseBody = webTestClient.get()
                .uri("tools/" + savedTool.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Tool.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(responseBody.getId(), savedTool.getId());
        Assertions.assertEquals(responseBody.getName(), savedTool.getName());
    }
}