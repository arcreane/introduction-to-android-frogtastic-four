package frog.frogtasticfour.tictactwo.data;

/**
 * Base X Y coordinates class that cooperates better with our project
 */
public class Point {
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x;
    public int y;

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }
}
