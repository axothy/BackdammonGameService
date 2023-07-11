package ru.axothy.backdammon.gameservice.service;

import ru.axothy.backdammon.gameservice.model.Player;

public interface PlayerService {
    Player getByNickname(String nickname);

    Player setStartValue(Player player, int value1, int value2);
    Player save(Player player);

    void delete(Player player);
}
