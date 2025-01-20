package frog.frogtasticfour.tictactwo;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import frog.frogtasticfour.tictactwo.R;
import frog.frogtasticfour.tictactwo.data.IDatabaseCallback;
import frog.frogtasticfour.tictactwo.data.Leaderboard;
import frog.frogtasticfour.tictactwo.data.LeaderboardUser;

public class LeaderboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;
    private Leaderboard leaderboard;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerView = findViewById(R.id.recycler_view_leaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progress_bar);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this::refreshLeaderboard);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        leaderboard = new Leaderboard();
        progressBar.setVisibility(View.VISIBLE);
        loadLeaderboard(false);
    }

    private void refreshLeaderboard() {
        swipeRefreshLayout.setRefreshing(true);
        loadLeaderboard(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadLeaderboard(boolean forceReload) {
        leaderboard.GetLeaderboard(forceReload, new IDatabaseCallback<>() {
            @Override
            public void OnSuccess(ArrayList<LeaderboardUser> leaderboardUsers) {
                if (leaderboardUsers != null && !leaderboardUsers.isEmpty()) {
                    adapter = new LeaderboardAdapter(leaderboardUsers);
                    recyclerView.setAdapter(adapter);
                }
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void OnFailure(Exception e) {
                e.printStackTrace();
                Toast.makeText(LeaderboardActivity.this, "Failed to load leaderboard", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
