package frog.frogtasticfour.tictactwo.data;

import frog.frogtasticfour.tictactwo.data.enums.CellValue;

public class Cell implements IGridable {
    private CellValue _value = CellValue.Empty;
    @Override
    public CellValue getValue() {
        return _value;
    }

    @Override
    public void setValue(CellValue value) {
        _value = value;
    }
}
