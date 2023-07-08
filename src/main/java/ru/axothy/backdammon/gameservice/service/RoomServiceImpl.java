package ru.axothy.backdammon.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.axothy.backdammon.gameservice.model.Player;
import ru.axothy.backdammon.gameservice.model.Room;
import ru.axothy.backdammon.gameservice.repos.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RoomServiceImpl implements RoomService {
    public static final int MAX_PLAYERS_IN_ROOM = 2;
    public static final int MIN_PLAYERS_IN_ROOM = 1;
    public static final int FIRST_PLAYER = 0;
    public static final int SECOND_PLAYER = 1;
    public static final int NO_BET = 0;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public Room getRoomById(int id) {
        return roomRepository.findById(id);
    }

    @Override
    public Room create(Player player) {
        Room room = new Room();
        room.setBet(NO_BET);
        room.getPlayers().add(player);
        return roomRepository.save(room);
    }

    @Override
    public Room create(int bet, Player player) {
        if (player.getBalance() < bet) return null;

        Room room = new Room();
        room.setBet(bet);
        room.getPlayers().add(player);
        return roomRepository.save(room);
    }

    @Override
    public Room create(int bet, int roomPassword, Player player) {
        if (player.getBalance() < bet) return null;

        Room room = new Room();
        room.setBet(bet);
        room.setRoomPassword(roomPassword);
        room.getPlayers().add(player);
        return roomRepository.save(room);
    }

    @Override
    public void delete(int roomId) {
        Room room = roomRepository.findById(roomId);
        roomRepository.delete(room);
    }

    @Override
    public Page<Room> getRooms(int page, int size) {
        return roomRepository.findByIsGameStartedFalse(PageRequest.of(page, size));
    }

    @Override
    public void joinRoom(int roomId, Player player) {
        Room room = getRoomById(roomId);
        if (room.isGameStarted() == false && room.getRoomPassword() == null && player.getBalance() >= room.getBet()
                && room.getPlayers().size() < MAX_PLAYERS_IN_ROOM) {
            room.getPlayers().add(player);
        }
    }

    @Override
    public void joinRoom(int roomId, int roomPassword, Player player) {
        Room room = getRoomById(roomId);
        if (room.isGameStarted() == false && player.getBalance() >= room.getBet()
                && room.getPlayers().size() < MAX_PLAYERS_IN_ROOM && room.getRoomPassword() == roomPassword) {
            room.getPlayers().add(player);
        }
    }

    @Override
    public void makePlayerReady(int roomId, String playerNickname) {
        Room room = roomRepository.findById(roomId);
        List<Player> playersInRoom = room.getPlayers();

        for(Player player : playersInRoom) {
            if (player.getNickname() == playerNickname) {
                player.setReady(true);
            }
        }

        if (playersInRoom.size() == MAX_PLAYERS_IN_ROOM) {
            if (playersInRoom.get(FIRST_PLAYER).isReady() == true && playersInRoom.get(SECOND_PLAYER).isReady() == true) {
                //TODO START GAME
            }
        }
    }

}
