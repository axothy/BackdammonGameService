package ru.axothy.backdammon.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.axothy.backdammon.gameservice.model.Board;
import ru.axothy.backdammon.gameservice.model.Chip;
import ru.axothy.backdammon.gameservice.model.Color;
import ru.axothy.backdammon.gameservice.model.Tower;
import ru.axothy.backdammon.gameservice.repos.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Service
public class BoardServiceImpl implements BoardService {
    private static final int NUMBER_OF_TOWERS = 24;
    private static final int INITIAL_TOWER_INDEX_BLACK = 12;
    private static final int INITIAL_TOWER_INDEX_WHITE = 0;
    private static final int NUMBER_OF_CHIPS = 15;

    @Autowired
    private BoardRepository boardRepository;

    @Override
    public Board createBoard() {
        Board board = new Board();
        board.setTowers(createTowers());

        return boardRepository.save(board);
    }

    private List<Tower> createTowers() {
        List<Tower> towers = new ArrayList<>();

        for(int i = 0; i < NUMBER_OF_TOWERS; i++) {
            Tower tower = new Tower();
            tower.setTowerNumberOnBoard(i);
            towers.add(tower);
        }

        towers.get(INITIAL_TOWER_INDEX_BLACK).setMainTower(true);
        towers.get(INITIAL_TOWER_INDEX_WHITE).setMainTower(true);

        towers.get(INITIAL_TOWER_INDEX_BLACK).setChips(createChips(Color.BLACK));
        towers.get(INITIAL_TOWER_INDEX_WHITE).setChips(createChips(Color.WHITE));

        return towers;
    }

    private List<Chip> createChips(Color color) {
        List<Chip> chips = new ArrayList<>();

        for(int i = 0; i < NUMBER_OF_CHIPS; i++) {
            Chip chip = new Chip();
            chip.setColor(color);
            chip.setChipNumberOnBoard(i);

            chips.add(chip);
        }

        return chips;
    }

    @Override
    public Board getBoardById(int id) {
        return boardRepository.findById(id);
    }

    @Override
    public void delete(int id) {
        Board board = getBoardById(id);
        boardRepository.delete(board);
    }

    @Override
    public Board update(Board board) {
        return boardRepository.save(board);
    }
}
