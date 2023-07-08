package ru.axothy.backdammon.gameservice.service;

import org.springframework.data.domain.Page;
import ru.axothy.backdammon.gameservice.model.Player;
import ru.axothy.backdammon.gameservice.model.Room;


public interface RoomService {

    Room getRoomById(int id);
    Page<Room> getRooms(int page, int size);

    Room joinRoom(int roomId, String nickname);

    void joinRoom(int roomId, int roomPassword, String nickname);

    Room create(String nickname);
    Room create(int bet, String nickname);
    Room create(int bet, int roomPassword, String nickname);
    void delete(int roomId);

    void makePlayerReady(int roomId, String nickname);
}
