package frog.frogtasticfour.tictactwo.data.enums;

import java.util.EnumSet;

public enum CellValue {
    Empty,
    X,
    O,
    Tie,
    Calculate;

    public boolean isPlayer() {
        return EnumSet.of(O, X).contains(this);
    }
}
