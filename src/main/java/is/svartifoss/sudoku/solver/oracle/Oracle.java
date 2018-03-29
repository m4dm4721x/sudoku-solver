package is.svartifoss.sudoku.solver.oracle;

import is.svartifoss.sudoku.model.CellFill;
import is.svartifoss.sudoku.model.Grid;

public interface Oracle {
    <T> CellFill<T> apply(final Grid<T> grid);
}
