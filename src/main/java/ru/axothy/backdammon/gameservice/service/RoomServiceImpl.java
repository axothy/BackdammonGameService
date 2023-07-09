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
import ru.axothy.backdammon.gameservice.model.Color;
import ru.axothy.backdammon.gameservice.model.ExistingPlayer;
import ru.axothy.backdammon.gameservice.model.Player;
import ru.axothy.backdammon.gameservice.model.Room;
import ru.axothy.backdammon.gameservice.repos.RoomRepository;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
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
    public void delete(int roomId) {
        Room room = roomRepository.findById(roomId);
        roomRepository.delete(room);
    }

    @Override
    public Page<Room> getRooms(int page, int size) {
        return roomRepository.findByIsGameStartedFalse(PageRequest.of(page, size));
    }

    //NEEDED CHECK PLAYER BALANCE
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
        player.setRoom(room);

        room.getPlayers().add(player);
        playerService.save(player);
        roomRepository.save(room);

        return room;
    }

    @Override
    public void joinRoom(int roomId, int roomPassword, String nickname) {
        Room room = getRoomById(roomId);
        //if (room.isGameStarted() == false && player.getBalance() >= room.getBet()
        //        && room.getPlayers().size() < MAX_PLAYERS_IN_ROOM && room.getRoomPassword() == roomPassword) {
        //room.getPlayers().add(player);
        //}
    }

    @Override
    public Room makePlayerReady(String nickname, boolean ready) {
        Player player = playerService.getByNickname(nickname);

        player.setReady(ready);

        Room room = player.getRoom();
        List<Player> players = room.getPlayers();

        if (players.size() == MAX_PLAYERS_IN_ROOM) {
            if (players.get(FIRST_PLAYER).isReady() == true && players.get(SECOND_PLAYER).isReady() == true) {
                room.setGameStarted(true);
                //TODO START GAME
            }
        }

        roomRepository.save(room);
        return room;
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
