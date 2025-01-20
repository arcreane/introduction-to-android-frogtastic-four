package frog.frogtasticfour.tictactwo.data;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class Leaderboard {
    private ArrayList<LeaderboardUser> _cache;
    private final Database _database;
    private static final String _leaderboardCollectionName = "leaderboard";
    private static final String _leaderboardScoreName = "score";
    private static final int _leaderboardSizeLimit = 10;

    public Leaderboard() {
        _database = new Database();
    }

    public void GetLeaderboard(IDatabaseCallback<ArrayList<LeaderboardUser>> callback) {
        GetLeaderboard(false, callback);
    }

    public void GetLeaderboard(boolean forceReload, IDatabaseCallback<ArrayList<LeaderboardUser>> callback) {
        if (_cache == null || forceReload)
            PopulateCache(callback);
        else {
            callback.OnSuccess(_cache);
        }
    }

    public void PopulateCache(IDatabaseCallback<ArrayList<LeaderboardUser>> callback) {
        var collection = _database.getCollection(_leaderboardCollectionName);
        var query = collection.orderBy(_leaderboardScoreName, Query.Direction.DESCENDING).limit(_leaderboardSizeLimit).get();
        _database.Build(query, new IDatabaseCallback<>() {
            @Override
            public void OnSuccess(QuerySnapshot snapshot) {
                if (_cache == null)
                    _cache = new ArrayList<>();
                _cache.clear();
                for (DocumentSnapshot document : snapshot.getDocuments()) {
                    String username = document.getId();
                    long score = document.getLong(_leaderboardScoreName);
                    _cache.add(new LeaderboardUser(username, score));
                }
                callback.OnSuccess(_cache);
            }

            @Override
            public void OnFailure(Exception e) {
                callback.OnFailure(e);
            }
        });
    }

    public static long CalculateScore(int depth, int size) {
        return Math.round(Math.pow(size, depth) * 100);
    }

    public void UpdateOrSetScore(String username, long score) {
        UpdateOrSetScore(username, score, new IDatabaseCallback<>() {
            @Override
            public void OnSuccess(Object document) {

            }

            @Override
            public void OnFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void UpdateOrSetScore(String username, long score, IDatabaseCallback<Object> callback) {
        UpdateOrSetScore(new LeaderboardUser(username, score), callback);
    }

    public void UpdateOrSetScore(LeaderboardUser leader, IDatabaseCallback<Object> callback) {
        var collection = _database.getCollection(_leaderboardCollectionName);
        _database.get(collection, leader.getUsername(), new IDatabaseCallback<>() {
            @Override
            public void OnSuccess(DocumentSnapshot document) {
                var oldScore = document.getLong(_leaderboardScoreName);
                if (oldScore == null) oldScore = 0L;

                _database.update(collection, leader.getUsername(), _leaderboardScoreName, oldScore + leader.getScore(), new IDatabaseCallback<>() {
                    @Override
                    public void OnSuccess(Object document) {
                        // SCORE UPDATED SUCCESSFULLY
                        callback.OnSuccess(document);
                    }

                    @Override
                    public void OnFailure(Exception e) {
                        callback.OnFailure(e);
                    }
                });
            }

            @Override
            public void OnFailure(Exception e) {
                if (Objects.requireNonNull(e.getMessage()).equalsIgnoreCase("document does not exist")) {
                    _database.set(collection, leader.getUsername(), leader.ToMap(), new IDatabaseCallback<>() {
                        @Override
                        public void OnSuccess(Object document) {
                            // SCORE ADDED SUCCESSFULLY FOR THE FIRST TIME
                        }

                        @Override
                        public void OnFailure(Exception e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
