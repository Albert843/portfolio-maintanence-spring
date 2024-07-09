package ru.gb.maintanence.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.gb.maintanence.model.Tool;
import ru.gb.maintanence.services.ToolService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("tools")
@RequiredArgsConstructor
public class ToolController {

    private final ToolService toolService;

    /**
     * Метод поиска всех инструментов по endpoint "tools"
     * @return статус ОК и список инструментов
     */
    @GetMapping()
    public ResponseEntity<List<Tool>> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(toolService.findAll());
    }

    /**
     * Метод поиска инструмента по id
     * @param id id инструмента
     * @return статус ОК и инструмент
     */
    @GetMapping("{id}")
    public ResponseEntity<Tool> findById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(toolService.findById(id));
    }

    /**
     * Метод добавления инструмента
     * @param tool инструмент
     * @return статус Created и инструмент
     */
    @PostMapping
    public ResponseEntity<Tool> createTool(@RequestBody Tool tool) {
        Tool createdTool = toolService.createTool(tool);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("id")
                .buildAndExpand(createdTool.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdTool);
    }

    /**
     * Метод обновления ирструмента по id
     * @param id  id инструмента
     * @param tool инструмент
     * @return статус ОК и интрумент
     */
    @PutMapping("{id}")
    public ResponseEntity<Tool> updateTool(@PathVariable long id, @RequestBody Tool tool) {
        return ResponseEntity.status(HttpStatus.OK).body(toolService.updateTool(id, tool));
    }

    /**
     * Метод удаления ресурса по id
     * @param id id инструмента
     * @return статус NoContent
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTool(@PathVariable long id) {
        toolService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
