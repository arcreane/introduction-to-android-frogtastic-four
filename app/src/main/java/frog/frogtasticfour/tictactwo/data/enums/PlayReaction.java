package frog.frogtasticfour.tictactwo.data.enums;

import java.util.EnumSet;

public enum PlayReaction {
    FailIllegalMove,
    FailNotEmpty,
    FailGameOver,
    SuccessBoard,
    SuccessCell,
    SuccessCellRelocated,
    SuccessGameEnd,
    Success;

    public boolean isSuccess() {
        return EnumSet.of(SuccessCell, Success, SuccessBoard, SuccessGameEnd, SuccessCellRelocated).contains(this);
    }
}
