package frog.frogtasticfour.tictactwo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText player1NameInput;
    private EditText player2NameInput;
    private Button startButton;
    private Button settingsButton;
    private ImageButton helpButton;
    private TextView gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Ensure the layout file matches your XML file name.

        // Initialize views
        player1NameInput = findViewById(R.id.player1Name);
        player2NameInput = findViewById(R.id.player2Name);
        startButton = findViewById(R.id.startButton);
        settingsButton = findViewById(R.id.settingButton);
        helpButton = findViewById(R.id.helpButton);
        gameName = findViewById(R.id.gameName);

        // Set up listeners for buttons
        startButton.setOnClickListener(view -> {
            String player1 = player1NameInput.getText().toString().trim();
            String player2 = player2NameInput.getText().toString().trim();

            if (player1.isEmpty() || player2.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter names for both players", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Game starting with " + player1 + " and " + player2, Toast.LENGTH_SHORT).show();
                //TODO: Add logic to navigate to the game screen or start the game
                startActivity(new Intent(this, GameActivity.class));
            }
        });

        settingsButton.setOnClickListener(view ->
                        Toast.makeText(MainActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show()
                //TODO: Add logic for navigating to settings or updating preferences
        );

        helpButton.setOnClickListener(view ->
                        Toast.makeText(MainActivity.this, "Help clicked", Toast.LENGTH_SHORT).show()
                //TODO: Add logic for displaying help information
        );
    }
}
