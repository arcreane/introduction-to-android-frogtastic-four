package frog.frogtasticfour.tictactwo.data;

public class Level {
    private Level _parent;
    private Level _child;
    private final Board _board;
    private int _depth;
    private Point _refPosition;

    public Level(Board board, int initialDepth, int x, int y) {
        this(board, initialDepth);
        _refPosition = new Point(x, y);
    }

    public Level(Board board, int initialDepth) {
        _board = board;
        _depth = initialDepth;
    }

    public int getDepth() {
        return _depth;
    }

    public boolean hasRelativePosition() {
        return _refPosition != null;
    }

    public Point getRelativePositionToParent() {
        return _refPosition;
    }

    public Level setParent(Level parent) {
        _parent = parent;
        return this;
    }

    public Level setChild(Level child) {
        _child = child;
        return this;
    }

    public Level getParent() {
        return _parent;
    }

    public Board getBoard() {
        return _board;
    }

    public boolean hasParent() {
        return _parent != null;
    }

    public boolean hasChild() {
        return _child != null;
    }

    public Level getFirstParent() {
        Level level = this;
        while (level.hasParent()) {
            level = level._parent;
        }
        return level;
    }

    public Level getLastChild() {
        Level level = this;
        while (level.hasChild()) {
            level = level._child;
        }
        return level;
    }
}
