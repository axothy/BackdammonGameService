package ru.axothy.backdammon.gameservice.service;

import ru.axothy.backdammon.gameservice.model.Chip;
import ru.axothy.backdammon.gameservice.model.Tower;

import java.util.List;

public interface TowerService {
    List<Tower> createTowers();
    Tower push(int towerId, Chip chip);
    Tower pop(int towerId);

}
