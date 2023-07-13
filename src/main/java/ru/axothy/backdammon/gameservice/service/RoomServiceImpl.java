package ru.axothy.backdammon.gameservice.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.axothy.backdammon.gameservice.config.KeycloakConfiguration;
import ru.axothy.backdammon.gameservice.model.*;
import ru.axothy.backdammon.gameservice.repos.RoomRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    private static final int NUMBER_OF_TOWERS = 24;
    private static final int INITIAL_TOWER_INDEX_BLACK = 23;
    private static final int INITIAL_TOWER_INDEX_WHITE = 11;
    private static final int NUMBER_OF_CHIPS = 15;
    public static final int MAX_PLAYERS_IN_ROOM = 2;
    public static final int MIN_PLAYERS_IN_ROOM = 1;
    public static final int FIRST_PLAYER = 0;
    public static final int SECOND_PLAYER = 1;
    public static final int NO_BET = 0;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TowerService towerService;

    @Autowired
    private Dice dice;

    @Autowired
    private KeycloakConfiguration keycloakConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Room getRoomById(int id) {
        return roomRepository.findById(id);
    }


    @Override
    public Room create(int bet, String nickname) {
        if (!(balanceCheck(nickname, bet)))
            return null;

        Player player = playerService.getByNickname(nickname);

        if (player == null) {
            player = new Player();
            player.setNickname(nickname);
        }

        if (player.getRoom() != null) return player.getRoom();

        player.setReady(false);
        player.setColor(Color.WHITE);
        player.setRoomOwner(true);

        Room room = new Room();

        room.setBet(bet);
        room.setPasswordEnabled(false);
        room.getPlayers().add(player);

        player.setRoom(room);

        roomRepository.save(room);
        playerService.save(player);

        return room;
    }

    private boolean balanceCheck(String nickname, int balance) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://localhost:8081/players")
                .queryParam("nickname", nickname);
        HttpEntity request = new HttpEntity(headers);


        ExistingPlayer player = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, ExistingPlayer.class).getBody();

        if (player.getBalance() >= balance)
            return true;
        else
            return false;
    }

    @Override
    public Room create(int bet, int roomPassword, String nickname) {
        //if (player.getBalance() < bet) return null;

        Room room = new Room();
        room.setBet(bet);
        room.setRoomPassword(roomPassword);
        //room.getPlayers().add(player);
        return roomRepository.save(room);
    }

    @Override
    public void delete(Room room) {
        roomRepository.delete(room);
    }

    @Override
    public void leaveRoom(String nickname) {
        Player player = playerService.getByNickname(nickname);
        Room room = player.getRoom();

        if (player.isRoomOwner())
            delete(room);
        else {
            room.getPlayers().remove(player);
            playerService.delete(player);
            roomRepository.save(room);
        }
    }

    @Override
    public Page<Room> getRooms(int page, int size) {
        return roomRepository.findByIsGameStartedFalse(PageRequest.of(page, size));
    }

    @Override
    public Room getRoomByPlayer(String nickname) {
        Player player = playerService.getByNickname(nickname);

        return player.getRoom();
    }

    @Override
    public Room joinRoom(int roomId, String nickname) {
        Room room = getRoomById(roomId);

        if (!(balanceCheck(nickname, room.getBet())))
            return null;

        Player player = playerService.getByNickname(nickname);

        if (player == null) {
            player = new Player();
            player.setNickname(nickname);
        }

        if (player.getRoom() != null || room.isGameStarted() || room.isPasswordEnabled()
                || room.getPlayers().size() >= MAX_PLAYERS_IN_ROOM)
            return null;


        player.setReady(false);
        player.setColor(Color.BLACK);
        player.setRoomOwner(false);
        player.setRoom(room);

        room.getPlayers().add(player);
        playerService.save(player);
        return roomRepository.save(room);
    }

    @Override
    public void joinRoom(int roomId, int roomPassword, String nickname) {

    }

    @Override
    public Room makePlayerReady(String nickname, boolean ready) {
        Player player = playerService.getByNickname(nickname);

        player.setReady(ready);

        Room room = player.getRoom();

        if (room.isGameStarted()) return null;

        List<Player> players = room.getPlayers();

        if (players.size() == MAX_PLAYERS_IN_ROOM) {
            if (bothPlayersAreReady(room)) {
                room.setGameStarted(true);
                createBoard(room);
                room.setWhoMoves(Color.WHITE);
            }
        }

        roomRepository.save(room);
        return room;
    }

    @Override
    public boolean bothPlayersAreReady(Room room) {
        List<Player> players = room.getPlayers();

        if (players.size() < MAX_PLAYERS_IN_ROOM) return false;
        else {
            if (players.get(FIRST_PLAYER).isReady() == true && players.get(SECOND_PLAYER).isReady() == true)
                return true;
        }

        return false;
    }

    @Override
    public Room createBoard(Room room) {
        room.setTowers(towerService.createTowers());

        return roomRepository.save(room);
    }

    @Override
    public Room startRoll(String nickname) {
        Player player = playerService.getByNickname(nickname);
        Room room = player.getRoom();

        if (room.getWhoMoves() != player.getColor() || isSomeoneMovesFirst(room)) return room;

        int valueFirst = dice.roll();
        int valueSecond = dice.roll();

        playerService.setDiceValues(player, valueFirst, valueSecond);
        changeSides(room);

        if (bothPlayersHasStartValue(room))
            checkWhoMovesFirst(room);


        return roomRepository.save(room);
    }

    @Override
    public Room roll(String nickname) {
        Player player = playerService.getByNickname(nickname);
        Room room = player.getRoom();

        if (room.getWhoMoves() != player.getColor()) return room;

        int valueFirst = dice.roll();
        int valueSecond = dice.roll();

        if (valueFirst == valueSecond)
            player.setMovesLeft(4);
        else
            player.setMovesLeft(2);

        playerService.setDiceValues(player, valueFirst, valueSecond);

        return roomRepository.save(room);
    }

    private void changeSides(Room room) {
        Color color = room.getWhoMoves();

        room.setWhoMoves(Color.opposite(color));
    }

    private boolean isSomeoneMovesFirst(Room room) {
        List<Player> players = room.getPlayers();

        for (Player player : players)
            if (player.isMovesFirst()) return true;

        return false;
    }

    private boolean bothPlayersHasStartValue(Room room) {
        List<Player> players = room.getPlayers();

        if (players.get(FIRST_PLAYER).getDiceValueFirst() != 0 && players.get(SECOND_PLAYER).getDiceValueSecond() != 0)
            return true;
        else
            return false;
    }

    private void checkWhoMovesFirst(Room room) {
        Player first = room.getPlayers().get(FIRST_PLAYER);
        Player second = room.getPlayers().get(SECOND_PLAYER);

        if ((first.getDiceValueFirst() + first.getDiceValueSecond()) <
                (second.getDiceValueFirst() + second.getDiceValueSecond())) {
            second.setMovesFirst(true);
            room.setWhoMoves(second.getColor());
        } else {
            first.setMovesFirst(true);
            room.setWhoMoves(first.getColor());
        }
    }

    @Override
    public Room move(String nickname, Move move) {
        Player player = playerService.getByNickname(nickname);
        Room room = player.getRoom();

        if (room.getWhoMoves() != player.getColor())
            return room;

        List<Integer> possibleMoves = getPossibleMoves(nickname, move.getIndexTowerFrom());
        if (!possibleMoves.contains(move.getIndexTowerTo())) return room;

        Tower towerFrom = room.getTowers().get(move.getIndexTowerFrom());
        Tower towerTo = room.getTowers().get(move.getIndexTowerTo());

        towerService.moveChip(towerFrom, towerTo);

        player.setMovesLeft(player.getMovesLeft() - 1);

        if (player.getMovesLeft() <= 0)
            changeSides(room);
        else
            playerService.changeDiceValue(player, move.getDiceValue());

        return roomRepository.save(room);
    }


    @Override
    public List<Integer> getPossibleMoves(String nickname, int towerFromIndex) {
        List<Integer> possibleMoves = new ArrayList<>();

        Player player = playerService.getByNickname(nickname);
        Room room = player.getRoom();
        Tower towerFrom = room.getTowers().get(towerFromIndex);

        if (towerFrom.getChips().isEmpty()) return Collections.emptyList();

        Chip chip = towerService.pop(towerFrom).get();

        if (player.getColor() != chip.getColor()) return Collections.emptyList();

        int towerToIndexFirst = -1, towerToIndexSecond = -1;

        if (chip.getColor() == Color.WHITE) {
            towerToIndexFirst = getIndexForDiceValueForWhites(player.getDiceValueFirst(), towerFromIndex);
            towerToIndexSecond = getIndexForDiceValueForWhites(player.getDiceValueSecond(), towerFromIndex);

        } else {
            towerToIndexFirst = getIndexForDiceValueForBlacks(player.getDiceValueFirst(), towerFromIndex);
            towerToIndexSecond = getIndexForDiceValueForBlacks(player.getDiceValueSecond(), towerFromIndex);
        }

        if (!isObstacleOnTower(room, towerToIndexFirst, player.getColor()))
                possibleMoves.add(towerToIndexFirst);
        if (!isObstacleOnTower(room, towerToIndexSecond, player.getColor()))
            possibleMoves.add(towerToIndexSecond);

        return possibleMoves;
    }

    private boolean booleanIsDouple(Player player) {
        if (player.getDiceValueFirst() == player.getDiceValueSecond())
            return true;
        else
            return false;
    }

    private boolean isObstacleOnTower(Room room, int towerToIndex, Color color) {
        if (towerService.pop(room.getTowers().get(towerToIndex)).isEmpty()) return false;

        if (towerService.pop(room.getTowers().get(towerToIndex)).get().getColor() == Color.opposite(color))
            return true;
        else
            return false;
    }

    private int getIndexForDiceValueForWhites(int diceValue, int towerFromIndex) {
        int towerToIndex;

        if (diceValue > 0) {
            if (towerFromIndex < diceValue)
                towerToIndex = INITIAL_TOWER_INDEX_BLACK - Math.abs(diceValue - towerFromIndex) + 1;
            else
                towerToIndex = towerFromIndex - diceValue;
        } else
            return -1;

        return towerToIndex;
    }

    private int getIndexForDiceValueForBlacks(int diceValue, int towerFromIndex) {
        if (diceValue > 0)
            return towerFromIndex - diceValue;
        else
            return -1;
    }

    private String getAdminToken() {
        Keycloak keycloak = KeycloakBuilder.builder().serverUrl(keycloakConfig.getAuthServerUrl())
                .grantType("password")
                .realm(keycloakConfig.getRealm())
                .clientId(keycloakConfig.getResource())
                .clientSecret(keycloakConfig.getClientSecret())
                .username(keycloakConfig.getUsername())
                .password(keycloakConfig.getPassword())
                .build();
        return keycloak.tokenManager().getAccessTokenString();
    }

}
