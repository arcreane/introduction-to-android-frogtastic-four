package frog.frogtasticfour.tictactwo;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import frog.frogtasticfour.tictactwo.data.Board;
import frog.frogtasticfour.tictactwo.data.Game;
import frog.frogtasticfour.tictactwo.data.IDatabaseCallback;
import frog.frogtasticfour.tictactwo.data.IGridable;
import frog.frogtasticfour.tictactwo.data.Leaderboard;
import frog.frogtasticfour.tictactwo.data.Level;
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

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private HashMap<CellValue, String> playerNames = new HashMap<>();
    private int size;
    private int depth;
    private Leaderboard leaderboard;
    private Level previewCache;
    private ImageButton homeButton;
    private ImageButton backButton;
    private ImageButton currentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        playerNames.put(CellValue.X, intent.getStringExtra("player1"));
        playerNames.put(CellValue.O, intent.getStringExtra("player2"));
        size = intent.getIntExtra("size", 3);
        depth = intent.getIntExtra("depth", 2);

        tvStatus = findViewById(R.id.tvStatus);
        tvDetails = findViewById(R.id.tvDetails);
        cachedTvDetails = findViewById(R.id.cachedTvStatus);
        gridBoard = findViewById(R.id.gridBoard);
        previousBoard = findViewById(R.id.previousBoard);
        Button btnRestart = findViewById(R.id.btnRestart);

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            previewCache = game.getCurrentLevelTree().clone();
            previewCache.setChild(null);
            reloadPreview();
        });
        backButton = findViewById(R.id.upButton);
        backButton.setOnClickListener(v -> {
            var lastChild = previewCache.getLastChild();
            if (lastChild == previewCache || !lastChild.hasParent())
                return;
            lastChild.getParent().setChild(null);
            reloadPreview();
        });

        currentButton = findViewById(R.id.currentButton);
        currentButton.setOnClickListener(v -> {
            goToCurrent();
        });

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        leaderboard = new Leaderboard();

        initializeGame();

        btnRestart.setOnClickListener(view -> {
            initializeGame();
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float angularVelocityX = event.values[0];
                float angularVelocityY = event.values[1];
                float angularVelocityZ = event.values[2];

                if (Math.abs(angularVelocityX) > 5.0 || Math.abs(angularVelocityY) > 5.0) {
                    initializeGame();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    private void initializeGame() {
        try {
            game = new Game(depth, size, CellValue.X);
            goToCurrent();
            var turn = game.getTurn();
            tvStatus.setText("Player " + playerNames.get(turn) + " (" + turn + ")'s Turn");
            tvDetails.setText("Depth: 0 | Reference Point: NULL");
            populateBoard();
        } catch (BoardException e) {
            tvStatus.setText("Error initializing game");
        }
    }

    private void goToCurrent() {
        previewCache = game.getCurrentLevel().clone();
        reloadPreview();
    }

    private void reloadPreview() {
        previousBoard.removeAllViews();
        int size = game.getCurrentLevel().getBoard().getSize();
        var level = previewCache.getLastChild();
        var refPos = level.getRelativePositionToParent();
        cachedTvDetails.setText("Depth: " + level.getDepth() + " | Reference: " + (level.hasRelativePosition() ? ("x = " + refPos.x + ", y = " + refPos.y) : "NULL"));
        previousBoard.setColumnCount(size);
        previousBoard.setRowCount(size);

        backButton.setEnabled(level.hasParent());

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                FrameLayout cellLayout = new FrameLayout(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(i);
                params.columnSpec = GridLayout.spec(j);
                cellLayout.setLayoutParams(params);

                // Create a TextView for the background "#" symbol
                TextView hashTextView = new TextView(this);
                hashTextView.setText("#");
                hashTextView.setTextSize(24f); // Adjust text size
                hashTextView.setTextColor(getResources().getColor(R.color.black));
                hashTextView.setGravity(Gravity.CENTER);

                Button valueButton = new Button(this);
                valueButton.setGravity(Gravity.CENTER); // Center align
                valueButton.setTextSize(24f); // Adjust text size
                valueButton.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20)); // White
                //valueButton.setLayoutParams(new GridLayout.LayoutParams());
                valueButton.setBackgroundColor(getResources().getColor(android.R.color.transparent)); // Make button background transparent
                valueButton.setBackgroundResource(R.drawable.border_top); // Top-left corner
                valueButton.setText("");
                IGridable cell = previewCache.getLastChild().getBoard().get(i, j);
                if (cell.getValue() != CellValue.Empty) {
                    hashTextView.setAlpha(0.4f);
                    valueButton.setText(cell.getValue().toString());
                }

                if (cell instanceof Board)
                    cellLayout.addView(hashTextView);
                else
                    valueButton.setEnabled(false);
                cellLayout.addView(valueButton);

                cellLayout.setTag(new int[]{i, j});
                valueButton.setTag(new int[]{i, j});
                valueButton.setOnClickListener(v -> {
                    Button cellButton = (Button) v;
                    int[] position = (int[]) cellButton.getTag();
                    int row = position[0];
                    int col = position[1];

                    var currentChild = previewCache.getLastChild();
                    var currentBoard = currentChild.getBoard();
                    if (currentBoard.getValue() != CellValue.Empty || currentBoard.containsCellable())
                    {
                        v.setEnabled(false);
                        return;
                    }
                    var createdLevel = new Level((Board) currentChild.getBoard().get(row, col), currentChild.getDepth() + 1, row, col);
                    createdLevel.setParent(currentChild);
                    currentChild.setChild(createdLevel);
                    reloadPreview();
                });

                previousBoard.addView(cellLayout);
            }
        }
    }

    private void populateBoard() {
        gridBoard.removeAllViews();
        int size = game.getCurrentLevel().getBoard().getSize();

        gridBoard.setColumnCount(size);
        gridBoard.setRowCount(size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                FrameLayout cellLayout = new FrameLayout(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(i);
                params.columnSpec = GridLayout.spec(j);
                cellLayout.setLayoutParams(params);

                // Create a TextView for the background "#" symbol
                TextView hashTextView = new TextView(this);
                hashTextView.setText("#");
                hashTextView.setTextSize(24f); // Adjust text size
                hashTextView.setTextColor(getResources().getColor(R.color.black));
                hashTextView.setGravity(Gravity.CENTER);

                Button valueButton = new Button(this);
                valueButton.setGravity(Gravity.CENTER); // Center align
                valueButton.setTextSize(24f); // Adjust text size
                valueButton.setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary20)); // White
                //valueButton.setLayoutParams(new GridLayout.LayoutParams());
                valueButton.setBackgroundColor(getResources().getColor(android.R.color.transparent)); // Make button background transparent
                valueButton.setBackgroundResource(R.drawable.border_top); // Top-left corner
                valueButton.setText("");
                IGridable cell = game.getCurrentLevel().getBoard().get(i, j);
                if (cell.getValue() != CellValue.Empty) {
                    hashTextView.setAlpha(0.4f);
                    valueButton.setText(cell.getValue().toString());
                }

                if (cell instanceof Board)
                    cellLayout.addView(hashTextView);
                cellLayout.addView(valueButton);

                cellLayout.setTag(new int[]{i, j});
                valueButton.setTag(new int[]{i, j});
                valueButton.setOnClickListener(this::handleCellClick);

                gridBoard.addView(cellLayout);
            }
        }
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
            case SuccessCellRelocated:
            case SuccessBoard:
                if (reaction != PlayReaction.SuccessBoard)
                    cellButton.setText(player.toString());
                populateBoard();
                break;
            case SuccessGameEnd:
                var winner = game.getWinner();
                var name = playerNames.get(winner);
                tvStatus.setText("Player " + name + " (" + winner + ") wins!");
                leaderboard.UpdateOrSetScore(name, Leaderboard.CalculateScore(depth, size), new IDatabaseCallback<>() {
                    @Override
                    public void OnSuccess(Object document) {
                        // SUCCESS
                    }

                    @Override
                    public void OnFailure(Exception e) {
                        Toast.makeText(GameActivity.this, "Failed to update leaderboard!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
                disableBoard();
                break;

            default:
                tvStatus.setText("Invalid move");
                break;
        }

        updateGameStatus();
        reloadPreview();
    }

    private void updateGameStatus() {
        CellValue winner = game.getWinner();
        if (winner == CellValue.X || winner == CellValue.O) {
            tvStatus.setText("Player " + playerNames.get(winner) + " (" + winner + ") wins!");
            disableBoard();
        } else if (winner == CellValue.Tie) {
            tvStatus.setText("It's a tie!");
            disableBoard();
        } else {
            var level = game.getCurrentLevel();
            var refPoint = level.getRelativePositionToParent();
            var turn = game.getTurn();
            tvStatus.setText("Player " + playerNames.get(turn) + " (" + turn + ")'s Turn");
            //cachedTvDetails.setText(tvDetails.getText());
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
