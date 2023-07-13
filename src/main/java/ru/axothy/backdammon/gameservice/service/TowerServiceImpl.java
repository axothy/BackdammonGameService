package ru.axothy.backdammon.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.axothy.backdammon.gameservice.model.Chip;
import ru.axothy.backdammon.gameservice.model.Color;
import ru.axothy.backdammon.gameservice.model.Tower;
import ru.axothy.backdammon.gameservice.repos.TowerRepository;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Tower push(Tower tower, Chip chip) {
        tower.getChips().add(chip);

        return towerRepository.save(tower);
    }

    @Override
    public Optional<Chip> pop(Tower tower) {
        return tower.getChips().isEmpty() ? Optional.empty() : Optional.of(tower.getChips().get(tower.getChips().size() - 1));
    }

    @Override
    public Chip removeChip(Tower tower) {
        Chip chip = pop(tower).get();

        tower.getChips().remove(chip);
        towerRepository.save(tower);

        return chip;
    }

    @Override
    public void moveChip(Tower towerFrom, Tower towerTo) {
        Chip chip = pop(towerFrom).get();

        towerFrom.getChips().remove(chip);
        towerTo.getChips().add(chip);

        towerRepository.save(towerFrom);
        towerRepository.save(towerTo);
    }
}
