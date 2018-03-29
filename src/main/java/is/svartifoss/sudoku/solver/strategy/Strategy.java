package is.svartifoss.sudoku.solver.strategy;

import is.svartifoss.sudoku.model.CellFill;
import is.svartifoss.sudoku.model.Grid;

import java.util.Optional;

public interface Strategy {
    <T> Optional<CellFill<T>> apply(final Grid<T> grid);
}
