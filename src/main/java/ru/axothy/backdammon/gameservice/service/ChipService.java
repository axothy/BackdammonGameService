package ru.axothy.backdammon.gameservice.service;

import ru.axothy.backdammon.gameservice.model.Chip;
import ru.axothy.backdammon.gameservice.model.Color;

import java.util.List;

public interface ChipService {
    List<Chip> createChips(Color color);
    void delete(int id);
}
