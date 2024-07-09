package ru.gb.maintanence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tools")
@Data
@NoArgsConstructor
public class Tool {

    /**
     * Id и стратегия генерации первичного ключа
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Имя колонки таблицы "name"
     */
    @Column(name = "name")
    private String name;

    /**
     * Конструктор
     */
    public Tool(String name) {
        this.name = name;
    }
}
