package frog.frogtasticfour.tictactwo.data;

import frog.frogtasticfour.tictactwo.data.enums.CellValue;
import frog.frogtasticfour.tictactwo.exceptions.GameException;

public class Game {
    private final Board _board;

    private CellValue _turn;
    private final Level _currentLevel;
    public Board getBoard() {
        return _board;
    }
    public boolean hasWinner() {
        return _board.getValue() != CellValue.Empty;
    }

    public Level getCurrentLevel() {
        return _currentLevel.getLastChild();
    }

    public CellValue getWinner() {
        return _board.getValue();
    }

    public CellValue getTurn() {
        return _turn;
    }

    public void swapTurn() {
        if (_turn == CellValue.X)
            _turn = CellValue.O;
        else
            _turn = CellValue.X;
    }

    public Game(int depth, int size, CellValue starting) {
        if (!starting.isPlayer())
            throw new GameException("Starting player has to be X or O");

        _board = Board.CreateBoard(depth, size);
        _turn = starting;
        _currentLevel = new Level(_board, 0);
    }
}
