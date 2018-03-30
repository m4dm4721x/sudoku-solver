package is.svartifoss.sudoku.strategy;

import is.svartifoss.sudoku.model.CellFill;
import is.svartifoss.sudoku.model.Sudoku;

import java.util.Optional;

public interface Strategy {
    <T> CellFill<T> apply(final Sudoku<T> sudoku);
}
