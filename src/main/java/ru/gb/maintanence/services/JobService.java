package ru.gb.maintanence.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.gb.maintanence.model.Job;
import ru.gb.maintanence.repositories.JobRepository;
import ru.gb.maintanence.repositories.ToolRepository;
import ru.gb.maintanence.repositories.WorkerRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    public static final String NOT_FOUND_MESSAGE = "Не удалось найти запись о работе с id=";

    /**
     * аннотация @Value определяет max количества инструмента
     * у одного работника на руках для работы.
     * значение = 2
     */
    @Value("${spring.application.job.max-allowed-tools:2}")
    private int maxAllowedTools;

    private final ToolRepository toolRepository;
    private final WorkerRepository workerRepository;
    private final JobRepository jobRepository;

    /**
     * Метод получения всех работ
     * @return список работ
     */
    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    /**
     * Метод получения работы по id
     * @param id id работы
     * @return работа
     */
    public Job findById(long id) {
        Job job = jobRepository.findById(id).orElse(null);
        if (job == null) {
            throwNotFoundExceptionById(id);
        }
        return job;
    }

    /**
     * Метод удаления работы по id
     * @param id id работы
     */
    public void deleteById(long id) {
        checkExistsById(id);
        jobRepository.deleteById(id);
    }

    /**
     * Метод создания работы
     * @param job id работы
     * @return созданная работа
     */
    public Job saveJob(Job job) {

        checkToolExists(job);
        checkWorkerExists(job);
        checkWorkersCountTool(job);

        Job savedJob = createJobAndSave(job);
        fillJob(savedJob);

        return savedJob;
    }

    /**
     * Метод получения работы по id
     * @param id id работы
     * @return работу
     */
    public Job returnTool(long id) {
        Job job = jobRepository.findById(id).orElse(null);
        if (job == null) {
            throwNotFoundExceptionById(id);
        }

        if (job.getReturnedAt() == null) {
            job.setReturnedAt(LocalDateTime.now());
            return jobRepository.save(job);
        }
        return job;
    }

    /**
     * Метод заполнения работы
     * @param job работа
     */
    private void fillJob(Job job) {
        job.setTool(toolRepository.findById(job.getTool().getId()).orElse(null));
        job.setWorker(workerRepository.findById(job.getWorker().getId()).orElse(null));
    }

    /**
     * Метод создания работы
     * @param job работа
     * @return сохраненую работу
     */
    private Job createJobAndSave(Job job) {
        job.setId(null);
        job.setTakenAt(LocalDateTime.now());
        job.setReturnedAt(null);

        return jobRepository.save(job);
    }

    /**
     * метод проверки наличия работы по id
     * @param id id работы
     */
    private void checkExistsById(long id) {
        if (!jobRepository.existsById(id)) {
            throwNotFoundExceptionById(id);
        }
    }

    /**
     * метод проверки количества инструмента у работника
     * @param job работа
     */
    private void checkWorkersCountTool(Job job) {
        long countToolsOnHand = jobRepository.countByWorkerAndReturnedAtIsNull(job.getWorker());
        if (countToolsOnHand > maxAllowedTools) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "У вас уже максимально допустимое количество инструмента на руках. Сдайте инструмент прежде, чем получите новый");
        }
    }

    /**
     * Метод для проверки корректности создания работы (наличие работника)
     * @param job работа
     */
    private void checkWorkerExists(Job job) {
        if (job.getWorker() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "не указано значение для поля worker");
        }
        if (!workerRepository.existsById(job.getWorker().getId())) {
            throwNotFoundExceptionById(job.getWorker().getId(), WorkerService.NOT_FOUND_MESSAGE);
        }
    }


    /**
     * Метод для проверки корректности создания работы (наличие инструмента)
     * @param job работа
     */
    private void checkToolExists(Job job) {
        if (job.getTool() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "не указано значение для поля tool");
        }
        if (!toolRepository.existsById(job.getTool().getId())) {
            throwNotFoundExceptionById(job.getTool().getId(), ToolService.NOT_FOUND_MESSAGE);
        }
    }

    /**
     * Метод перехвата исключения для генерации сообщения клиенту ошибки 404
     * @param id работы
     */
    private void throwNotFoundExceptionById(long id) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE + id);
    }

    /**
     * Метод перехвата исключения для генерации сообщения клиенту ошибки 404 и текста ошибки
     * @param id id работы
     * @param message сообщение
     */
    private void throwNotFoundExceptionById(long id, String message) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, message + id);
    }
}
