package ru.gb.maintanence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "workers")
@Data
@NoArgsConstructor
public class Worker {

    /**
     * Id и стратегия генерации первичного ключа
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Имя колонки таблицы "surname"
     */
    @Column(name = "surname")
    private String surname;

    /**
     * Имя колонки таблицы "name"
     */
    @Column(name = "name")
    private String name;

    /**
     * Конструктор
     */
    public Worker(String surname, String name) {
        this.surname = surname;
        this.name = name;
    }
}
