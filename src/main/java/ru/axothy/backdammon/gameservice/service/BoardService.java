package ru.axothy.backdammon.gameservice.service;

import ru.axothy.backdammon.gameservice.model.Board;
import ru.axothy.backdammon.gameservice.model.Room;

public interface BoardService {
    Board createBoard(Room room);

    Board getBoardById(int id);

    Board getBoardByPlayer(String nickname);


    Board startRoll(String nickname);

    void delete(int id);
    Board update(Board board);

}
