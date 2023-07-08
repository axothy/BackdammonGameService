package ru.axothy.backdammon.gameservice.controller;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ru.axothy.backdammon.gameservice.config.KeycloakConfiguration;
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

    private String retrieveNickname(String id) {
        UsersResource usersResource = keycloak.realm(keycloakConfiguration.getRealm()).users();
        UserResource userResource = usersResource.get(id);

        return userResource.toRepresentation().getUsername();
    }

}
