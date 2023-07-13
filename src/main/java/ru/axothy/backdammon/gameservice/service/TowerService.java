package ru.axothy.backdammon.gameservice.service;

import ru.axothy.backdammon.gameservice.model.Chip;
import ru.axothy.backdammon.gameservice.model.Tower;

import java.util.List;
import java.util.Optional;

public interface TowerService {
    List<Tower> createTowers();
    Tower push(Tower tower, Chip chip);
    Optional<Chip> pop(Tower tower);
    Chip removeChip(Tower tower);

    void moveChip(Tower towerFrom, Tower towerTo);


}
