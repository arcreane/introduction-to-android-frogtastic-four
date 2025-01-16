package frog.frogtasticfour.tictactwo.data;

import frog.frogtasticfour.tictactwo.data.enums.CellValue;

public class Board implements IGridable{
    private CellValue _value = CellValue.Empty;
    private final IGridable[][] _grid;

    public Board(IGridable[][] grid) {
        _grid = grid;
    }

    private static final MatrixCreator<Cell> _cellCreator = new MatrixCreator<>(Cell::new, Cell.class);

    public static Board CreateBoard() {
        return CreateBoard(2, 3);
    }

    public static Board CreateBoard(int levels) {
        return CreateBoard(levels, 3);
    }

    public static Board CreateBoard(int levels, int size) {
        var interiorLevel = levels - 1;
        var boardCreator = new MatrixCreator<>(() -> CreateBoard(interiorLevel, size), Board.class);
        if (interiorLevel == 0) //its cellable
            return new Board(_cellCreator.Generate(size, size));
        else
            return new Board(boardCreator.Generate(size, size));
    }

    @Override
    public CellValue getValue() {
        return _value;
    }

    @Override
    public void setValue(CellValue value) {
        _value = value;
    }
}
