package ru.axothy.backdammon.gameservice.service;

import org.springframework.data.domain.Page;
import ru.axothy.backdammon.gameservice.model.Player;
import ru.axothy.backdammon.gameservice.model.Room;

import java.util.List;

public interface RoomService {

    Room getRoomByName(String name);
    Page<Room> getRooms(int page, int size);

    void joinRoom(String roomName, Player player);

    void joinRoom(String roomName, int roomPassword, Player player);

    Room create(String roomName, Player player);
    Room create(String roomName, int bet, Player player);
    Room create(String roomName, int bet, int roomPassword, Player player);
    void delete(int roomId);

    void makePlayerReady(int roomId, String playerNickname);
}
