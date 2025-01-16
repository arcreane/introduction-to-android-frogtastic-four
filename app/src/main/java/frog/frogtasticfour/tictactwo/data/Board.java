package frog.frogtasticfour.tictactwo.data;

import frog.frogtasticfour.tictactwo.data.enums.CellValue;

public class Board implements IGridable{
    private CellValue _value = CellValue.Empty;
    private final IGridable[][] _grid;

    public Board(IGridable[][] grid) {
        _grid = grid;
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
