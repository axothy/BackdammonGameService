package ru.axothy.backdammon.gameservice.repos;

import org.springframework.data.repository.CrudRepository;
import ru.axothy.backdammon.gameservice.model.Chip;
import ru.axothy.backdammon.gameservice.model.Tower;

import java.util.List;

public interface ChipRepository extends CrudRepository<Chip, Integer> {
}
