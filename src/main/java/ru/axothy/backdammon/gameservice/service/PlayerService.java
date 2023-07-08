package ru.axothy.backdammon.gameservice.service;

import ru.axothy.backdammon.gameservice.model.Player;

public interface PlayerService {
    Player getByNickname(String nickname);
    Player save(Player player);
}
