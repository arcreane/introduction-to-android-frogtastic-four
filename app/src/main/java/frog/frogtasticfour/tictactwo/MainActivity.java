package frog.frogtasticfour.tictactwo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import frog.frogtasticfour.tictactwo.data.enums.CellValue;

public class MainActivity extends AppCompatActivity {

    private EditText player1NameInput;
    private EditText player2NameInput;
    private Button startButton;
    private Button settingsButton;
    //    private ImageButton helpButton;
    private  Button helpButton;
    private TextView gameName;

    private EditText depthInput;
    private EditText sizeInput;


    private void startGame(String player1, String player2, int size, int depth) {
        Toast.makeText(MainActivity.this, "Game starting with " + player1 + " and " + player2, Toast.LENGTH_SHORT).show();
        var intent = new Intent(this, GameActivity.class);
        intent.putExtra("player1", player1);
        intent.putExtra("player2", player2);
        intent.putExtra("size", size);
        intent.putExtra("depth", depth);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        player1NameInput = findViewById(R.id.player1Name);
        player2NameInput = findViewById(R.id.player2Name);
        startButton = findViewById(R.id.startButton);
        settingsButton = findViewById(R.id.leaderboardButton);
        helpButton = findViewById(R.id.helpButton);
        gameName = findViewById(R.id.gameName);

        depthInput = findViewById(R.id.DepthNumber);
        sizeInput = findViewById(R.id.SizeNumber);


        startButton.setOnClickListener(view -> {
            String player1 = player1NameInput.getText().toString().trim();
            String player2 = player2NameInput.getText().toString().trim();

            String depths = depthInput.getText().toString().trim();
            String sizes = sizeInput.getText().toString().trim();

            int depth;
            if(depths.isEmpty()) {
                depth = 2;
            } else {
                depth = Integer.parseInt(depths);
            }

            int size;
            if(sizes.isEmpty()) {
                size = 2;
            } else {
                size = Integer.parseInt(sizes);
            }


            if (player1.isEmpty() || player2.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter names for both players", Toast.LENGTH_SHORT).show();
            } else if (size < 1 || depth < 1) {
                Toast.makeText(MainActivity.this, "Size and depth need to be at least 1", Toast.LENGTH_SHORT).show();
            } else if (depth * size > 20) {
                new AlertDialog.Builder(this)
                        .setTitle("Your Phone Is Scared")
                        .setMessage("Do you hate your phone? (Size and depth too big. Ideally you want a total of less than 20 when multiplied)")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> startGame(player1, player2, size, depth))
                        .setNegativeButton(android.R.string.no, null).show();
            }
            else {
                startGame(player1, player2, size, depth);
            }
        });

        settingsButton.setOnClickListener(view -> {
            startActivity(new Intent(this, LeaderboardActivity.class));
        });

        helpButton.setOnClickListener(view -> {

            // Make the fragment container visible
            if (findViewById(R.id.nav_host_fragment).getVisibility() == View.GONE) {
                findViewById(R.id.nav_host_fragment).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.nav_host_fragment).setVisibility(View.GONE);
            }


            // Replace the fragment in the container
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new FirstFragment())
                    .commit();
        });

    }
}