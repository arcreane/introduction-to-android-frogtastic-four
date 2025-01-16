package frog.frogtasticfour.tictactwo.data;

import java.lang.reflect.Array;
import java.util.function.Supplier;

public class MatrixCreator<T> {
    private final Supplier<T> _supplier;
    private final Class<T> _clazz;

    public MatrixCreator(Supplier<T> supplier, Class<T> clazz) {
        _supplier = supplier;
        _clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    public T[][] Generate(int rows, int columns) {
        var _matrix = (T[][]) Array.newInstance(_clazz, rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                _matrix[i][j] = _supplier.get();
            }
        }

        return _matrix;
    }
}
