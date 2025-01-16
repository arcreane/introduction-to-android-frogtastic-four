package frog.frogtasticfour.tictactwo.data;

import frog.frogtasticfour.tictactwo.data.enums.CellValue;

public class Game {
    private final Board _board;

    private CellValue _turn;
    public Board getBoard() {
        return _board;
    }
    public boolean hasWinner() {
        return _board.getValue() != CellValue.Empty;
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


}
