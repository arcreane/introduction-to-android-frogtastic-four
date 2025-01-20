package frog.frogtasticfour.tictactwo.data;

import java.util.HashMap;

public class LeaderboardUser {
    public LeaderboardUser(String username, long score) {
        _username = username;
        _score = score;
    }

    public long getScore() {
        return _score;
    }

    public String getUsername() {
        return _username;
    }

    private final String _username;
    private final long _score;

    public HashMap<String, Object> ToMap() {
        var map = new HashMap<String, Object>();
        //map.put("username", _username);
        map.put("score", _score);
        return map;
    }
}
