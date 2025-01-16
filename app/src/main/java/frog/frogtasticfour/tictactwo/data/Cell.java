package frog.frogtasticfour.tictactwo.data;

import frog.frogtasticfour.tictactwo.data.enums.CellValue;
import frog.frogtasticfour.tictactwo.exceptions.CellValueException;

public class Cell implements IGridable {
    private CellValue _value = CellValue.Empty;
    @Override
    public CellValue getValue() {
        return _value;
    }

    @Override
    public void setValue(CellValue value) {
        if (_value != CellValue.Empty)
            throw new CellValueException("This cell was already set, you cannot change the value");
        if (value == CellValue.Calculate)
            throw new CellValueException("You cannot calculate the winner of a cell");
        if (value == CellValue.Tie)
            throw new CellValueException("Cell can't tie");
        _value = value;
    }
}
