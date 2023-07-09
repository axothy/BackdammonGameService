package ru.axothy.backdammon.gameservice.controller;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.axothy.backdammon.gameservice.config.KeycloakConfiguration;
import ru.axothy.backdammon.gameservice.model.Player;
import ru.axothy.backdammon.gameservice.model.Room;
import ru.axothy.backdammon.gameservice.service.PlayerService;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/room")
public class PlayerController {
    @Autowired
    private KeycloakConfiguration keycloakConfiguration;

    @Autowired
    private Keycloak keycloak;

    @Autowired
    private PlayerService playerService;

    @RolesAllowed({"PLAYER", "ADMIN"})
    @GetMapping(value = "/player")
    public ResponseEntity<Room> getPlayerRoom(Principal principal) {
        Player player = playerService.getByNickname(retrieveNickname(principal.getName()));

        return ResponseEntity.ok(player.getRoom());
    }

    private String retrieveNickname(String id) {
        UsersResource usersResource = keycloak.realm(keycloakConfiguration.getRealm()).users();
        UserResource userResource = usersResource.get(id);

        return userResource.toRepresentation().getUsername();
    }
}
