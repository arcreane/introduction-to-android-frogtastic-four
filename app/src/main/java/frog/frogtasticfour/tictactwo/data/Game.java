package frog.frogtasticfour.tictactwo.data;

import frog.frogtasticfour.tictactwo.data.enums.CellValue;
import frog.frogtasticfour.tictactwo.data.enums.PlayReaction;
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

    public PlayReaction setLevel(int row, int column) {
        var size = _board.getSize();
        if (row > size || column > size)
            return PlayReaction.FailIllegalMove;
        var currentChild = _currentLevel.getLastChild();
        var currentBoard = currentChild.getBoard();
        if (currentBoard.getValue() != CellValue.Empty || currentBoard.containsCellable())
            return PlayReaction.FailNotEmpty; // cant enter a cell or something that already has a value
        var createdLevel = new Level((Board) currentChild.getBoard().get(row, column), currentChild.getDepth() + 1, row, column);
        createdLevel.setParent(currentChild);
        currentChild.setChild(createdLevel);
        return PlayReaction.Success;
    }
}
