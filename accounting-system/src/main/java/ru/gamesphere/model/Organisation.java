package ru.gamesphere.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Organisation {
    private int id;
    private String name;
    private int inn;
    private int bankAccount;
}
