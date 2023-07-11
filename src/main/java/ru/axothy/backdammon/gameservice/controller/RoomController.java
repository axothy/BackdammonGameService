package ru.axothy.backdammon.gameservice.controller;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import ru.axothy.backdammon.gameservice.config.KeycloakConfiguration;
import ru.axothy.backdammon.gameservice.model.Player;
import ru.axothy.backdammon.gameservice.model.Room;
import ru.axothy.backdammon.gameservice.service.PlayerService;
import ru.axothy.backdammon.gameservice.service.RoomService;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private Keycloak keycloak;

    @Autowired
    private KeycloakConfiguration keycloakConfiguration;

    @RolesAllowed({"PLAYER", "ADMIN"})
    @GetMapping(value = "/list", params = {"page", "size"})
    public List<Room> getRooms(@RequestParam int page, @RequestParam int size) {
        List<Room> rooms = roomService.getRooms(page, size).getContent();

        return rooms;
    }

    @RolesAllowed({"PLAYER", "ADMIN"})
    @GetMapping(value = "/test")
    public String test(Principal principal) {
        return retrieveNickname(principal.getName());
    }

    @RolesAllowed({"PLAYER", "ADMIN"})
    @PostMapping(value = "/create_no_password", params = {"bet"})
    public ResponseEntity<Room> createNewRoomNoPassword(Principal principal, @RequestParam int bet) {
        Room newRoom = roomService.create(bet, retrieveNickname(principal.getName()));

        if (newRoom == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(newRoom);
    }

    @RolesAllowed({"PLAYER", "ADMIN"})
    @PostMapping(value = "/join/{id}")
    public ResponseEntity<Room> joinRoomNoPassword(@PathVariable("id") Integer id, Principal principal) {
        Room room = roomService.joinRoom(id, retrieveNickname(principal.getName()));

        if (room == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(room);
    }

    @RolesAllowed({"PLAYER", "ADMIN"})
    @PostMapping(value = "/ready", params = {"ready"})
    public ResponseEntity<Room> makePlayerReady(Principal principal, @RequestParam Boolean ready) {
        Room room = roomService.makePlayerReady(retrieveNickname(principal.getName()), ready);

        return ResponseEntity.ok(room);
    }

    @RolesAllowed({"PLAYER", "ADMIN"})
    @GetMapping("/checkReady")
    public DeferredResult<ResponseEntity<Room>> checkReady(Principal principal) {
        String nickname = retrieveNickname(principal.getName());
        Player player = playerService.getByNickname(nickname);

        DeferredResult<ResponseEntity<Room>> output = new DeferredResult<>();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            if (roomService.bothPlayersAreReady(player.getRoom().getRoomId())) {
                Room room = roomService.getRoomById(player.getRoom().getRoomId());
                output.setResult(ResponseEntity.ok(room));

                executor.shutdownNow();

            }

        }, 0, 1, TimeUnit.SECONDS);


        return output;
    }

    private String retrieveNickname(String id) {
        UsersResource usersResource = keycloak.realm(keycloakConfiguration.getRealm()).users();
        UserResource userResource = usersResource.get(id);

        return userResource.toRepresentation().getUsername();
    }

    @RolesAllowed({"PLAYER", "ADMIN"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/leave")
    public void leaveRoom(Principal principal) {
        roomService.leaveRoom(retrieveNickname(principal.getName()));

    }

}
