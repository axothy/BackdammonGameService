package ru.axothy.backdammon.gameservice.repos;

import org.springframework.data.repository.CrudRepository;
import ru.axothy.backdammon.gameservice.model.Board;

public interface BoardRepository extends CrudRepository<Board, Integer> {
    Board findById(int id);
}
