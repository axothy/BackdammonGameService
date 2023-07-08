package ru.axothy.backdammon.gameservice.repos;

import org.springframework.data.repository.CrudRepository;
import ru.axothy.backdammon.gameservice.model.Player;


public interface PlayerRepository extends CrudRepository<Player, Integer> {
    Player findByNickname(String nickname);
}
