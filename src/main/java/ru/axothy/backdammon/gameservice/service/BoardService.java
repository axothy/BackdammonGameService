package ru.axothy.backdammon.gameservice.service;

import ru.axothy.backdammon.gameservice.model.Board;
import ru.axothy.backdammon.gameservice.model.Tower;

import java.util.List;

public interface BoardService {
    Board createBoard();
    Board getBoardById(int id);

    void delete(int id);
    Board update(Board board);

}
