package ru.axothy.backdammon.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.axothy.backdammon.gameservice.model.Chip;
import ru.axothy.backdammon.gameservice.model.Color;
import ru.axothy.backdammon.gameservice.model.Tower;
import ru.axothy.backdammon.gameservice.repos.TowerRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TowerServiceImpl implements TowerService {
    private static final int NUMBER_OF_TOWERS = 24;
    private static final int INITIAL_TOWER_INDEX_BLACK = 23;
    private static final int INITIAL_TOWER_INDEX_WHITE = 11;

    @Autowired
    private TowerRepository towerRepository;

    @Autowired
    private ChipService chipService;

    @Override
    public List<Tower> createTowers() {
        List<Tower> towers = new ArrayList<>();

        for(int i = 0; i < NUMBER_OF_TOWERS; i++) {
            Tower tower = new Tower();
            tower.setTowerNumberOnBoard(i);
            towers.add(tower);
        }

        towers.get(INITIAL_TOWER_INDEX_BLACK).setMainTower(true);
        towers.get(INITIAL_TOWER_INDEX_WHITE).setMainTower(true);

        towers.get(INITIAL_TOWER_INDEX_BLACK).setChips(chipService.createChips(Color.BLACK));
        towers.get(INITIAL_TOWER_INDEX_WHITE).setChips(chipService.createChips(Color.WHITE));

        return (List<Tower>) towerRepository.saveAll(towers);
    }

    @Override
    public Tower push(int towerId, Chip chip) {
        Tower tower = towerRepository.findById(towerId).get();
        tower.getChips().add(chip);

        return towerRepository.save(tower);
    }

    @Override
    public Tower pop(int towerId) {
        Tower tower = towerRepository.findById(towerId).get();
        tower.getChips().remove(tower.getChips().size() - 1);

        return towerRepository.save(tower);
    }
}
