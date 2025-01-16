package frog.frogtasticfour.tictactwo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import frog.frogtasticfour.tictactwo.data.Game;
import frog.frogtasticfour.tictactwo.data.IGridable;
import frog.frogtasticfour.tictactwo.data.enums.CellValue;
import frog.frogtasticfour.tictactwo.data.enums.PlayReaction;
import frog.frogtasticfour.tictactwo.exceptions.BoardException;

public class GameActivity extends AppCompatActivity {

    private Game game;
    private TextView tvStatus;
    private TextView tvDetails;
    private TextView cachedTvDetails;
    private GridLayout gridBoard;
    private GridLayout previousBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvStatus = findViewById(R.id.tvStatus);
        tvDetails = findViewById(R.id.tvDetails);
        cachedTvDetails = findViewById(R.id.cachedTvStatus);
        gridBoard = findViewById(R.id.gridBoard);
        previousBoard = findViewById(R.id.previousBoard);
        Button btnRestart = findViewById(R.id.btnRestart);

        initializeGame();

        btnRestart.setOnClickListener(view -> initializeGame());
    }

    private void initializeGame() {
        try {
            game = new Game(2, 3, CellValue.X); // 3-depth nested boards, 3x3 size
            tvStatus.setText("Player X's Turn");
            tvDetails.setText("Depth: 0 | Reference Point: NULL");
            cachedTvDetails.setText("");
            previousBoard.removeAllViews();
            populateBoard();
        } catch (BoardException e) {
            tvStatus.setText("Error initializing game");
        }
    }

    private void saveCurrentBoardToPreviousGrid() {
        previousBoard.removeAllViews();

        for (int i = 0; i < gridBoard.getChildCount(); i++) {
            View view = gridBoard.getChildAt(i);

            if (view instanceof Button) {
                Button originalButton = (Button) view;
                Button newButton = new Button(this);

                // Copy the original button's properties
                newButton.setLayoutParams(originalButton.getLayoutParams());
                newButton.setText(originalButton.getText());
                newButton.setEnabled(false); // Previous grid is for display, not interaction

                // Add the new button to the previousGrid
                previousBoard.addView(newButton);
            }
        }

        // Set column and row counts to match gridBoard
        previousBoard.setColumnCount(gridBoard.getColumnCount());
        previousBoard.setRowCount(gridBoard.getRowCount());
    }

    private void populateBoard() {
        if (gridBoard.getChildCount() > 0) {
            // Save the current board into previousGrid
            saveCurrentBoardToPreviousGrid();
        }

        gridBoard.removeAllViews();
        int size = game.getCurrentLevel().getBoard().getSize();

        gridBoard.setColumnCount(size);
        gridBoard.setRowCount(size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Button cellButton = new Button(this);
                cellButton.setLayoutParams(new GridLayout.LayoutParams());
                cellButton.setText("");
                cellButton.setTag(new int[]{i, j});
                cellButton.setOnClickListener(this::handleCellClick);

                IGridable cell = game.getCurrentLevel().getBoard().get(i, j);
                if (cell.getValue() != CellValue.Empty) {
                    cellButton.setText(cell.getValue().toString());
                }

                gridBoard.addView(cellButton);
            }
        }

        // Enable or disable the back button based on the current level
        //btnBackLevel.setEnabled(game.getCurrentLevel().hasParent());
    }

    private void handleCellClick(View view) {
        Button cellButton = (Button) view;
        int[] position = (int[]) cellButton.getTag();
        int row = position[0];
        int col = position[1];

        var player = game.getTurn();
        PlayReaction reaction = game.playTurn(row, col);
        switch (reaction) {
            case SuccessCell:
                // Update the button with the current player's symbol
                cellButton.setText(player.toString());
                break;


            case SuccessCellRelocated:
                cellButton.setText(player.toString());
                populateBoard();
                break;

            case SuccessBoard:
                // Transition to the child board
                populateBoard();
                break;

            case SuccessGameEnd:
                tvStatus.setText("Player " + game.getWinner() + " wins!");
                disableBoard();
                break;

            default:
                // Handle illegal moves or other reactions
                tvStatus.setText("Invalid move");
                break;
        }

        updateGameStatus();
    }

    private void handleBackLevel() {
        if (game.goBackLevel()) {
            populateBoard();
        }
    }

    private void updateGameStatus() {
        CellValue winner = game.getWinner();
        if (winner == CellValue.X || winner == CellValue.O) {
            tvStatus.setText("Player " + winner + " wins!");
            disableBoard();
        } else if (winner == CellValue.Tie) {
            tvStatus.setText("It's a tie!");
            disableBoard();
        } else {
            var level = game.getCurrentLevel();
            var refPoint = level.getRelativePositionToParent();
            tvStatus.setText("Player " + game.getTurn() + "'s Turn");
            cachedTvDetails.setText(tvDetails.getText());
            if (level.hasRelativePosition())
                tvDetails.setText("Depth: " + level.getDepth() + " | Reference: x = " + refPoint.x + ", y = " + refPoint.y);
            else
                tvDetails.setText("Depth: " + level.getDepth() + " | Reference: NULL");
        }
    }

    private void disableBoard() {
        for (int i = 0; i < gridBoard.getChildCount(); i++) {
            gridBoard.getChildAt(i).setEnabled(false);
        }
    }
}
