package ru.axothy.backdammon.gameservice.service;

import org.springframework.data.domain.Page;
import ru.axothy.backdammon.gameservice.model.Room;


public interface RoomService {

    Room getRoomById(int id);
    Page<Room> getRooms(int page, int size);

    Room getRoomByPlayer(String nickname);

    Room joinRoom(int roomId, String nickname);

    void joinRoom(int roomId, int roomPassword, String nickname);

    boolean bothPlayersAreReady(Room room);

    Room create(int bet, String nickname);
    Room create(int bet, int roomPassword, String nickname);

    Room createBoard(Room room);
    void delete(Room room);

    void leaveRoom(String nickname);

    Room makePlayerReady(String nickname, boolean ready);

    Room startRoll(String nickname);
}
