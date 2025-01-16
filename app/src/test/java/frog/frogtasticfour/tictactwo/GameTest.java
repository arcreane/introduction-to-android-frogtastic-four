package frog.frogtasticfour.tictactwo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import frog.frogtasticfour.tictactwo.data.Board;
import frog.frogtasticfour.tictactwo.data.Game;
import frog.frogtasticfour.tictactwo.data.enums.CellValue;
import frog.frogtasticfour.tictactwo.data.enums.PlayReaction;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GameTest {
    @Test
    public void checkLevelGoUp() {
        var board = new Game(3, 3, CellValue.X);
        assertTrue(board.setLevel(0, 0).isSuccess());

        assertSame(board.getCurrentLevel().getDepth(), 1);

        assertTrue(board.setLevel(0, 0).isSuccess());

        assertSame(board.getCurrentLevel().getDepth(), 2);

        var res = board.goBackLevel();

        assertTrue(res);
        assertSame(board.getCurrentLevel().getDepth(), 1);
    }

    @Test
    public void checkSetLevelCell() {
        var board = new Game(1, 3, CellValue.X);
        var res = board.setLevel(0, 0);
        assertFalse(res.isSuccess());
    }

    @Test
    public void checkSetLevelBoard() {
        var board = new Game(2, 3, CellValue.X);
        var res = board.setLevel(0, 0);
        assertTrue(res.isSuccess());
        assertSame(board.getCurrentLevel().getDepth(), 1);
    }

    @Test
    public void checkRespectfulEnforcement() {
        var board = new Game(2, 3, CellValue.X);
        var res1 = board.playTurn(0, 0); // X BOARD
        assertSame(res1, PlayReaction.SuccessBoard);
        var res2 = board.playTurn(0, 0); // X CELL
        assertSame(res2, PlayReaction.SuccessCellRelocated);
        var res3 = board.playTurn(0, 1); // O CELL
        assertSame(res3, PlayReaction.SuccessCellRelocated);
        assertSame(((Board)board.getBoard().get(0, 0)).get(0, 1).getValue(), CellValue.O);
    }

    @Test
    public void checkWin() throws Exception {
        var board = new Game(2, 3, CellValue.X);
        var res1 = board.playTurn(1, 1); // X BOARD
        var res2 = board.playTurn(1, 1); // X CELL

        var res3 = board.playTurn(0, 1); // O CELL

        var res4 = board.playTurn(0, 0); // x CELL

        var res5 = board.playTurn(1, 1); // O CELL

        var res6 = board.playTurn(0, 0); // x CELL

        var res7 = board.playTurn(0, 1); // O CELL

        var res8 = board.playTurn(0, 1); // x CELL

        var res9 = board.playTurn(1, 1); // O CELL

        var res10 = board.playTurn(2, 2); // x CELL

        // X NOW WINS MIDDLE BOARD (1, 1 OF THE BIG BOARD)

        var res11 = board.playTurn(0, 1); // o CELL

        var res12 = board.playTurn(0, 2); // X CELL

        // X NOW WINS top middle BOARD (0, 1 OF THE BIG BOARD)

        var res13 = board.playTurn(2, 1); // o CELL

        var res14 = board.playTurn(1, 1); // x CELL

        //free PICK FOR O
        var res15 = board.playTurn(0, 0); // O board
        var res16 = board.playTurn(2, 1); // O CELL

        // O wins TOP LEFT BOARD (0,0 of the big board)

        var res17 = board.playTurn(1, 0); // x CELL

        var res18 = board.playTurn(2, 1); // O CELL

        var res19 = board.playTurn(1, 2); // x CELL

        // x wins bottom middle board (2, 1 of the big board)

        assertSame(res19, PlayReaction.SuccessGameEnd);

        var res20 = board.getBoard().getValue();

        assertTrue(board.hasWinner());
        assertSame(board.getWinner(), CellValue.X);
    }
}