package is.svartifoss.sudoku.model;

import is.svartifoss.sudoku.model.Grid;

public interface CellFill<T> {
    Grid<T> apply();

    Grid<T> revert();

    boolean isNonDeterministic();
}
