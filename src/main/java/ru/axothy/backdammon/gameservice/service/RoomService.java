package ru.axothy.backdammon.gameservice.service;

import org.springframework.data.domain.Page;
import ru.axothy.backdammon.gameservice.model.Player;
import ru.axothy.backdammon.gameservice.model.Room;


public interface RoomService {

    Room getRoomById(int id);
    Page<Room> getRooms(int page, int size);

    void joinRoom(int roomId, Player player);

    void joinRoom(int roomId, int roomPassword, Player player);

    Room create(Player player);
    Room create(int bet, Player player);
    Room create(int bet, int roomPassword, Player player);
    void delete(int roomId);

    void makePlayerReady(int roomId, String playerNickname);
}
