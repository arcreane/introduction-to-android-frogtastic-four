package frog.frogtasticfour.tictactwo.data;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class Leaderboard {
    private ArrayList<LeaderboardUser> _cache;
    private final Database _database;

    public Leaderboard() {
        _database = new Database();
    }

    public static long CalculateScore(int depth, int size) {
        return Math.round(Math.pow(size, depth) * 100);
    }
}
