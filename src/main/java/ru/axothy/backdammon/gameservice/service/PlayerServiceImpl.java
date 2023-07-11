package ru.axothy.backdammon.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.axothy.backdammon.gameservice.model.Player;
import ru.axothy.backdammon.gameservice.repos.PlayerRepository;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private PlayerRepository playerRepository;
    @Override
    public Player getByNickname(String nickname) {
        return playerRepository.findByNickname(nickname);
    }

    @Override
    public Player save(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public Player setStartValue(Player player, int value1, int value2) {
        player.setStartValueFirst(value1);
        player.setStartValueSecond(value1);

        return playerRepository.save(player);
    }

    @Override
    public void delete(Player player) {
        playerRepository.delete(player);
    }
}
