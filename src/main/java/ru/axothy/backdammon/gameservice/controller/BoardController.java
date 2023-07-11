package ru.axothy.backdammon.gameservice.controller;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import ru.axothy.backdammon.gameservice.config.KeycloakConfiguration;
import ru.axothy.backdammon.gameservice.model.Board;
import ru.axothy.backdammon.gameservice.model.Room;
import ru.axothy.backdammon.gameservice.model.Tower;
import ru.axothy.backdammon.gameservice.service.BoardService;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping(value = "/board")
public class BoardController {
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    private Keycloak keycloak;

    @Autowired
    private KeycloakConfiguration keycloakConfiguration;

    @Autowired
    private BoardService boardService;

    @RolesAllowed({"PLAYER", "ADMIN"})
    @PostMapping(value = "/startroll")
    public ResponseEntity<Room> startRoll(Principal principal) {
        Board board = boardService.startRoll(retrieveNickname(principal.getName()));

        return ResponseEntity.ok(board.getRoom());
    }



    private String retrieveNickname(String id) {
        UsersResource usersResource = keycloak.realm(keycloakConfiguration.getRealm()).users();
        UserResource userResource = usersResource.get(id);

        return userResource.toRepresentation().getUsername();
    }
}
