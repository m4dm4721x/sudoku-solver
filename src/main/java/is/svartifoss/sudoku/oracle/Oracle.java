package is.svartifoss.sudoku.oracle;

import is.svartifoss.sudoku.model.CellFill;
import is.svartifoss.sudoku.model.Sudoku;

public interface Oracle {
    <T> CellFill<T> apply(final Sudoku<T> sudoku);
}
