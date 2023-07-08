package ru.axothy.backdammon.gameservice.controller;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.axothy.backdammon.gameservice.config.KeycloakConfiguration;
import ru.axothy.backdammon.gameservice.model.Player;
import ru.axothy.backdammon.gameservice.model.Room;
import ru.axothy.backdammon.gameservice.service.RoomService;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/room")
public class RoomController {
    @Autowired
    private RoomService roomService;

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
    @PostMapping(value = "/create_no_password")
    public ResponseEntity<Room> createNewRoomNoPassword(Principal principal) {
        Room newRoom = roomService.create(retrieveNickname(principal.getName()));

        return ResponseEntity.ok(newRoom);
    }

    @RolesAllowed({"PLAYER", "ADMIN"})
    @PostMapping(value = "/join/{id}")
    public ResponseEntity<Room> joinRoomNoPassword(@PathVariable("id") Integer id, Principal principal) {
        Room room = roomService.joinRoom(id, retrieveNickname(principal.getName()));

        return ResponseEntity.ok(room);
    }

    private String retrieveNickname(String id) {
        UsersResource usersResource = keycloak.realm(keycloakConfiguration.getRealm()).users();
        UserResource userResource = usersResource.get(id);

        return userResource.toRepresentation().getUsername();
    }

}
