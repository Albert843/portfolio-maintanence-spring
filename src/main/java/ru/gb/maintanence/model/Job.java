package ru.gb.maintanence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
public class Job {

    /**
     * Id и стратегия генерации первичного ключа
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя колонки таблицы "id_worker" и ссылка на колонку "id" таблицы Worker
     */
    @ManyToOne
    @JoinColumn(name = "id_worker", referencedColumnName = "id")
    private Worker worker;

    /**
     * Имя колонки таблицы "id_tool" и ссылка на колонку "id" таблицы Tool
     */
    @ManyToOne
    @JoinColumn(name = "id_tool", referencedColumnName = "id")
    private Tool tool;

    /**
     * Время забора инструмента и начало работы
     */
    @Column(name = "taken_at")
    private LocalDateTime takenAt;

    /**
     * Время возврата инструмента и конец работы
     */
    @Column(name = "returned_at")
    private LocalDateTime returnedAt;
}
