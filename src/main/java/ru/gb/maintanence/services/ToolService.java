package ru.gb.maintanence.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.gb.maintanence.model.Tool;
import ru.gb.maintanence.repositories.ToolRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToolService {

    public static final String NOT_FOUND_MESSAGE = "Не удалось найти инструмент с id=";

    private final ToolRepository toolRepository;

    /**
     * Метод слушает событие при загрузке приложения
     * и создает в таблице "tools" список инструментов
     */
    @EventListener(ContextRefreshedEvent.class)
    public void onCreateDatabase() {
        toolRepository.save(new Tool("Перфоратор"));
        toolRepository.save(new Tool("Дрель"));
        toolRepository.save(new Tool("Шлиф-машинка"));
        toolRepository.save(new Tool("Сабельная пила"));
        toolRepository.save(new Tool("Гайковерт"));
        toolRepository.save(new Tool("Лобзик"));
        toolRepository.save(new Tool("Фен"));
    }

    /**
     * Метод получения списка инструментов
     */
    public List<Tool> findAll() {
        return toolRepository.findAll();
    }

    /**
     * Метод поиска инструмента по id
     * @param id id инструмента
     * @return инстумент по id
     */
    public Tool findById(long id){
        Tool tool = toolRepository.findById(id).orElse(null);
        if (tool == null){
            throwNotFoundExceptionById(id);
        }
        return tool;
    }

    /**
     * Метод удаления инструмента по id
     * @param id id инструмента
     */
    public void deleteById(long id) {
        checkExistsById(id);
        toolRepository.deleteById(id);
    }

    /**
     * Метод создания инструмента
     * @param tool инструмент
     * @return инструмент
     */
    public Tool createTool(Tool tool){
        tool.setId(0);
        return toolRepository.save(tool);
    }

    /**
     * Метод обновления инструмента по id
     * @param id id инструмента
     * @param tool инструмент
     * @return инструмент
     */
    public Tool updateTool(long id, Tool tool) {
        checkExistsById(id);
        tool.setId(id);
        return toolRepository.save(tool);
    }

    /**
     * Метод проверки существования инструмента по id
     * @param id id инструмента
     */
    private void checkExistsById(long id){
        if (!toolRepository.existsById(id)){
            throwNotFoundExceptionById(id);
        }
    }

    /**
     * Метод перехвата исключения и вывода сообщения клиенту ошибка 404
     * @param id id инструмента
     */
    private void throwNotFoundExceptionById(long id){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE + id);
    }

}
