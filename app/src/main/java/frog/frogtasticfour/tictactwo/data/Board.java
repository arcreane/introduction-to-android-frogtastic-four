package frog.frogtasticfour.tictactwo.data;

import androidx.annotation.NonNull;

import frog.frogtasticfour.tictactwo.data.enums.CellValue;
import frog.frogtasticfour.tictactwo.exceptions.BoardException;
import frog.frogtasticfour.tictactwo.exceptions.CellValueException;

public class Board implements IGridable{
    private CellValue _value = CellValue.Empty;
    private final IGridable[][] _grid;

    public IGridable get(int row, int column) {
        return _grid[row][column];
    }

    public boolean containsCellable() {
        return _grid[0][0] instanceof Cell;
    }

    public int getSize() {
        return _grid.length; //row and columns will be the same so this works
    }

    public Board(IGridable[][] grid) {
        _grid = grid;
    }

    private static final MatrixCreator<Cell> _cellCreator = new MatrixCreator<>(Cell::new, Cell.class);

    public static Board CreateBoard() throws BoardException {
        return CreateBoard(2, 3);
    }

    public static Board CreateBoard(int levels) throws BoardException {
        return CreateBoard(levels, 3);
    }

    public static Board CreateBoard(int levels, int size) throws BoardException {
        if (levels == 0)
            throw new BoardException("Board is too small");

        var interiorLevel = levels - 1;
        var boardCreator = new MatrixCreator<>(() -> CreateBoard(interiorLevel, size), Board.class);
        if (interiorLevel == 0) //its cellable
            return new Board(_cellCreator.Generate(size, size));
        else
            return new Board(boardCreator.Generate(size, size));
    }

    private CellValue calculateWinner() {
        var size = _grid.length;
        boolean diag1 = _grid[0][0].getValue() != CellValue.Empty;
        boolean diag2 = _grid[0][size - 1].getValue() != CellValue.Empty;
        CellValue winner = CellValue.Empty;

        for (int s = 1; s < size; s++) {
            if (_grid[s][s].getValue() != _grid[0][0].getValue()) {
                diag1 = false;
            }
            if (_grid[s][size - 1 - s].getValue() != _grid[0][size - 1].getValue()) {
                diag2 = false;
            }
        }
        if (diag1) {
            winner = _grid[0][0].getValue();
        } else if (diag2) {
            winner = _grid[0][size - 1].getValue();
        }

        for (int i = 0; i < size; i++) {
            if (_grid[i][i].getValue() != CellValue.Empty) {
                boolean row = true;
                boolean col = true;
                for (int j = 0; j < size - 1; j++) {
                    if (_grid[i][j].getValue() != _grid[i][j + 1].getValue()) {
                        row = false;
                    }
                    if (_grid[j][i].getValue() != _grid[j + 1][i].getValue()) {
                        col = false;
                    }
                }
                if (row || col) {
                    winner = _grid[i][i].getValue();
                }
            }
        }
        if (winner != CellValue.Empty) {
            return winner;
        }

        boolean empty = false;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (_grid[i][j].getValue() == CellValue.Empty) {
                    empty = true;
                }
            }
        }
        if (empty) {
            return CellValue.Empty;
        } else {
            return CellValue.Tie;
        }
    }

    @Override
    public CellValue getValue() {
        return _value;
    }

    @Override
    public void setValue(CellValue value) {
        if (_value != CellValue.Empty)
            throw new CellValueException("This board already has a value");
        if (value == CellValue.Calculate) {
            CellValue winner = calculateWinner();
            if (winner == CellValue.X || winner == CellValue.O || winner == CellValue.Tie) {
                _value = winner;
            }
        } else
            _value = value;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (IGridable[] iGridables : _grid) {
            for (int col = 0; col < iGridables.length; col++) {
                IGridable cell = iGridables[col];
                String value = cell.getValue().toString(); // Get the value as a string
                builder.append(value);

                if (col < iGridables.length - 1) {
                    builder.append("|"); // Add a separator between columns
                }
            }
            builder.append("\n"); // New line at the end of each row
        }

        return builder.toString();
    }
}
