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
    public Player setDiceValues(Player player, int value1, int value2) {
        player.setDiceValueFirst(value1);
        player.setDiceValueSecond(value2);

        return playerRepository.save(player);
    }

    @Override
    public void changeDiceValue(Player player, int diceValue) {
        if (player.getMovesLeft() > 1) return;

        if (player.getDiceValueFirst() == diceValue)
            player.setDiceValueFirst(-1);
        else if (player.getDiceValueSecond() == diceValue)
            player.setDiceValueSecond(-1);


        playerRepository.save(player);
    }

    @Override
    public void delete(Player player) {
        playerRepository.delete(player);
    }
}
