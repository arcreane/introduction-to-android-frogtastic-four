package frog.frogtasticfour.tictactwo.data;

import frog.frogtasticfour.tictactwo.data.enums.CellValue;

public interface IGridable {
    public CellValue getValue();
    public void setValue(CellValue value);
}
