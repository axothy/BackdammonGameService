package ru.axothy.backdammon.gameservice.service;

import ru.axothy.backdammon.gameservice.model.Chip;

public interface TowerService {
    void push(int towerId, Chip chip);
    Chip pop(int towerId);

}
