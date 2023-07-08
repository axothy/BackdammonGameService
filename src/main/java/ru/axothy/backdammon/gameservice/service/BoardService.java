package ru.axothy.backdammon.gameservice.service;

import ru.axothy.backdammon.gameservice.model.Board;

public interface BoardService {
    Board createBoard();
    Board getBoardById(int id);

    void delete(int id);
    Board update(Board board);

}
