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

    private Point _lastPlayedPoint;

    public boolean hasWinner() {
        return _board.getValue() != CellValue.Empty;
    }

    public Level getCurrentLevel() {
        return _currentLevel.getLastChild();
    }

    public Level getCurrentLevelTree() {
        return _currentLevel;
    }
    
    public boolean goBackLevel() {
        var lastChild = _currentLevel.getLastChild();
        if (lastChild == _currentLevel || !lastChild.hasParent())
            return false;
        lastChild.getParent().setChild(null);
        //lastChild.getParent().getParent().setChild(lastChild.getParent());
        return true;
    }

    private boolean calculateRespectPastPoint() {
        if (_lastPlayedPoint == null)
            return false;

        var currentChild = getCurrentLevel();

        if (currentChild.hasParent()) {
            var parent = currentChild.getParent();
            if (!parent.getBoard().get(_lastPlayedPoint.x, _lastPlayedPoint.y).getValue().equals(CellValue.Empty))
                return false;
        }

        return true;
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

    public PlayReaction playTurn(int row, int column) {
        if (hasWinner())
            return PlayReaction.FailGameOver;

        var currentChild = _currentLevel.getLastChild();
        var currentBoard = currentChild.getBoard();
        IGridable currentThing = currentBoard.get(row, column); //MAKE SURE IT IS EMPTY BEFORE PLAYING

        if (currentThing.getValue() != CellValue.Empty)
            return PlayReaction.FailNotEmpty;

        if (currentBoard.containsCellable()) {
            currentThing.setValue(_turn);

            _lastPlayedPoint = new Point(row, column);

            currentBoard.setValue(CellValue.Calculate);
            if (currentBoard.getValue() != CellValue.Empty)
            {
                // Value was changed winner detected of miniboard
                var parent = currentChild.getParent();
                while (parent != null) {
                    var parentBoard = parent.getBoard();
                    parentBoard.setValue(CellValue.Calculate);

                    if (parentBoard.getValue() != CellValue.Empty)
                        parent = parent.getParent();
                    else break;
                }
            }

            // MUST BE HERE TO AVOID WRITING TO A FULL CELL
            var tempCalc = calculateRespectPastPoint();
            boolean relocated = false;
            goBackLevel();
            if (tempCalc) {
                setLevel(_lastPlayedPoint.x, _lastPlayedPoint.y);
                relocated = true;
            }

            if (hasWinner()) {
                return PlayReaction.SuccessGameEnd;
            }
            swapTurn();
            return relocated ? PlayReaction.SuccessCellRelocated : PlayReaction.SuccessCell;
        } else {
            setLevel(row, column);
            return PlayReaction.SuccessBoard;
        }
    }
}
