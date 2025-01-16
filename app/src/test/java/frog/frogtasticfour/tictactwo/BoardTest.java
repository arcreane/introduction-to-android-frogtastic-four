package frog.frogtasticfour.tictactwo;

import org.junit.Test;

import static org.junit.Assert.*;

import frog.frogtasticfour.tictactwo.data.Board;
import frog.frogtasticfour.tictactwo.data.enums.CellValue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BoardTest {
    @Test
    public void checkDiagonalRight() {
        var board = Board.CreateBoard(1, 4);
        board.get(0, 3).setValue(CellValue.X);
        board.get(1, 2).setValue(CellValue.X);
        board.get(2, 1).setValue(CellValue.X);
        board.get(3, 0).setValue(CellValue.X);
        board.setValue(CellValue.Calculate);
        assertSame(board.getValue(), CellValue.X);
    }

    @Test
    public void checkDiagonalLeft() {
        var board = Board.CreateBoard(1, 4);
        board.get(0, 0).setValue(CellValue.X);
        board.get(1, 1).setValue(CellValue.X);
        board.get(2, 2).setValue(CellValue.X);
        board.get(3, 3).setValue(CellValue.X);
        board.setValue(CellValue.Calculate);
        assertSame(board.getValue(), CellValue.X);
    }

    @Test
    public void checkFourthRow() {
        var board = Board.CreateBoard(1, 4);
        board.get(3, 0).setValue(CellValue.X);
        board.get(3, 1).setValue(CellValue.X);
        board.get(3, 2).setValue(CellValue.X);
        board.get(3, 3).setValue(CellValue.X);
        board.setValue(CellValue.Calculate);
        assertSame(board.getValue(), CellValue.X);
    }

    @Test
    public void checkFourthColumn() {
        var board = Board.CreateBoard(1, 4);
        board.get(0, 3).setValue(CellValue.X);
        board.get(1, 3).setValue(CellValue.X);
        board.get(2, 3).setValue(CellValue.X);
        board.get(3, 3).setValue(CellValue.X);
        board.setValue(CellValue.Calculate);
        assertSame(board.getValue(), CellValue.X);
    }

    @Test
    public void checkTie() {
        var board = Board.CreateBoard(1, 4);
        board.get(0, 0).setValue(CellValue.X);
        board.get(0, 1).setValue(CellValue.O);
        board.get(0, 2).setValue(CellValue.X);
        board.get(0, 3).setValue(CellValue.O);
        board.get(1, 0).setValue(CellValue.O);
        board.get(1, 1).setValue(CellValue.O);
        board.get(1, 2).setValue(CellValue.X);
        board.get(1, 3).setValue(CellValue.X);
        board.get(2, 0).setValue(CellValue.X);
        board.get(2, 1).setValue(CellValue.X);
        board.get(2, 2).setValue(CellValue.O);
        board.get(2, 3).setValue(CellValue.O);
        board.get(3, 0).setValue(CellValue.O);
        board.get(3, 1).setValue(CellValue.X);
        board.get(3, 2).setValue(CellValue.O);
        board.get(3, 3).setValue(CellValue.X);
        board.setValue(CellValue.Calculate);
        assertSame(board.getValue(), CellValue.Tie);
    }

    @Test
    public void checkRegular() {
        var board = Board.CreateBoard(1, 3);
        board.get(0, 0).setValue(CellValue.X);
        board.get(0, 1).setValue(CellValue.O);
        board.get(0, 2).setValue(CellValue.X);
        board.get(1, 0).setValue(CellValue.O);
        board.get(1, 1).setValue(CellValue.O);
        board.get(1, 2).setValue(CellValue.X);
        board.get(2, 0).setValue(CellValue.O);
        board.get(2, 1).setValue(CellValue.X);
        board.get(2, 2).setValue(CellValue.O);
        board.setValue(CellValue.Calculate);
        assertSame(board.getValue(), CellValue.Tie);
    }
}