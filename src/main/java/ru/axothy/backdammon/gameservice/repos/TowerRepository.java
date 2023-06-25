package ru.axothy.backdammon.gameservice.repos;

import org.springframework.data.repository.CrudRepository;
import ru.axothy.backdammon.gameservice.model.Tower;

public interface TowerRepository extends CrudRepository<Tower, Integer> {
}
