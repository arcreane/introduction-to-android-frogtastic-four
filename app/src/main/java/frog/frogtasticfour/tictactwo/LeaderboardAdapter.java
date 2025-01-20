package frog.frogtasticfour.tictactwo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import frog.frogtasticfour.tictactwo.R;
import frog.frogtasticfour.tictactwo.data.LeaderboardUser;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    private final ArrayList<LeaderboardUser> leaderboardUsers;

    public LeaderboardAdapter(ArrayList<LeaderboardUser> leaderboardUsers) {
        this.leaderboardUsers = leaderboardUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaderboardUser user = leaderboardUsers.get(position);

        // Add medals for the top 3 positions
        String medal = "";
        if (position == 0) {
            medal = "🥇 ";
        } else if (position == 1) {
            medal = "🥈 ";
        } else if (position == 2) {
            medal = "🥉 ";
        } else {
            medal = "   ";
        }

        holder.usernameTextView.setText(medal + user.getUsername());
        holder.scoreTextView.setText(String.valueOf(user.getScore()));
    }

    @Override
    public int getItemCount() {
        return leaderboardUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView scoreTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.text_username);
            scoreTextView = itemView.findViewById(R.id.text_score);
        }
    }
}
