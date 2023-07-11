package ru.axothy.backdammon.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.axothy.backdammon.gameservice.model.Chip;
import ru.axothy.backdammon.gameservice.model.Color;
import ru.axothy.backdammon.gameservice.repos.ChipRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChipServiceImpl implements ChipService {
    private static final int NUMBER_OF_CHIPS = 15;

    @Autowired
    private ChipRepository chipRepository;

    @Override
    public List<Chip> createChips(Color color) {
        List<Chip> chips = new ArrayList<>();

        for(int i = 0; i < NUMBER_OF_CHIPS; i++) {
            Chip chip = new Chip();
            chip.setColor(color);
            chip.setChipNumberOnBoard(i);

            chips.add(chip);
        }

        return (List<Chip>) chipRepository.saveAll(chips);
    }

    @Override
    public void delete(int id) {
        Chip chip = chipRepository.findById(id).get();
        chipRepository.delete(chip);
    }


}
