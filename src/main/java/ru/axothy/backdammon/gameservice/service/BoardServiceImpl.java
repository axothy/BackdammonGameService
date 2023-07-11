package ru.axothy.backdammon.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import ru.axothy.backdammon.gameservice.model.*;
import ru.axothy.backdammon.gameservice.repos.BoardRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    private static final int NUMBER_OF_TOWERS = 24;
    private static final int INITIAL_TOWER_INDEX_BLACK = 23;
    private static final int INITIAL_TOWER_INDEX_WHITE = 11;
    private static final int NUMBER_OF_CHIPS = 15;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TowerService towerService;

    @Autowired
    private ChipService chipService;

    @Autowired
    private PlayerService playerService;

    @Override
    public Board createBoard(Room room) {
        Board board = new Board();
        board.setTowers(towerService.createTowers());

        return boardRepository.save(board);
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

    @Override
    public Board startRoll(String nickname) {
        int valueFirst = Dice.roll();
        int valueSecond = Dice.roll();

        Player player = playerService.getByNickname(nickname);
        Room room = player.getRoom();
        Board board = room.getBoard();

        if (board.getWhoMoves() != null) return board;

        playerService.setStartValue(player, valueFirst, valueSecond);

        if (bothPlayersHasStartValue(room)) {
            Player first = room.getPlayers().get(0);
            Player second = room.getPlayers().get(1);

            checkWhoMovesFirst(first, second, board);
        }

        return board;
    }

    @Override
    public Board getBoardByPlayer(String nickname) {
        Player player = playerService.getByNickname(nickname);

        return player.getRoom().getBoard();
    }

    private boolean bothPlayersHasStartValue(Room room) {
        List<Player> players = room.getPlayers();

        if (players.get(0).getStartValueFirst() != 0 && players.get(1).getStartValueFirst() != 0)
            return true;
        else
            return false;
    }

    private void checkWhoMovesFirst(Player first, Player second, Board board) {

        if ((first.getStartValueFirst() + first.getStartValueSecond()) <
                (second.getStartValueFirst() + second.getStartValueSecond())) {
            second.setMovesFirst(true);
            board.setWhoMoves(second.getColor());
        }
        else {
            first.setMovesFirst(true);
            board.setWhoMoves(first.getColor());
        }
    }
}
