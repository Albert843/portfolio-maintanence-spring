package ru.gb.maintanence.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.gb.maintanence.model.Tool;
import ru.gb.maintanence.model.Worker;
import ru.gb.maintanence.repositories.WorkerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerService {

    public static final String NOT_FOUND_MESSAGE = "Не удалось найти работника с id=";

    private final WorkerRepository workerRepository;

    /**
     * Метод слушает событие при загрузке приложения
     * и создает в таблице "workers" список работников
     */
    @EventListener(ContextRefreshedEvent.class)
    public void onCreateDatabase() {
        workerRepository.save(new Worker("Иванов", "Глеб"));
        workerRepository.save(new Worker("Петров", "Иван"));
        workerRepository.save(new Worker("Васильев", "Семен"));
        workerRepository.save(new Worker("Каменев", "Александр"));
        workerRepository.save(new Worker("Синичкин", "Станислав"));
    }

    /**
     * Метод получения списка работников
     */
    public List<Worker> findAll() {
        return workerRepository.findAll();
    }

    /**
     * Метод поиска работника по id
     * @param id id работника
     * @return работника
     */
    public Worker findById(long id){
        Worker worker = workerRepository.findById(id).orElse(null);
        if (worker == null){
            throwNotFoundExceptionById(id);
        }
        return worker;
    }

    /**
     * Метод удаления работника по id
     * @param id id работника
     */
    public void deleteById(long id) {
        checkExistsById(id);
        workerRepository.deleteById(id);
    }

    /**
     * Метод создания работника
     * @param worker работник
     * @return работника
     */
    public Worker createWorker(Worker worker){
        worker.setId(0);
        return workerRepository.save(worker);
    }

    /**
     * Метод обновления работника по id
     * @param id id работника
     * @param worker работник
     * @return работника
     */
    public Worker updateWorker(long id, Worker worker) {
        checkExistsById(id);
        worker.setId(id);
        return workerRepository.save(worker);
    }

    /**
     * Метод проверки существования работника по id
     * @param id работника
     */
    private void checkExistsById(long id){
        if (!workerRepository.existsById(id)){
            throwNotFoundExceptionById(id);
        }
    }

    /**
     * Метод перехвата исключения и вывода сообщения клиенту ошибка 404
     * @param id id работника
     */
    private void throwNotFoundExceptionById(long id){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE + id);
    }
}
