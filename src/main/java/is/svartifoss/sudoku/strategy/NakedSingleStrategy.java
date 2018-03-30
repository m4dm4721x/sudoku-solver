package is.svartifoss.sudoku.strategy;

import is.svartifoss.sudoku.model.Cell;
import is.svartifoss.sudoku.model.DeterministicCellFill;
import is.svartifoss.sudoku.model.Sudoku;

public class NakedSingleStrategy implements Strategy {
    @Override
    public <T> DeterministicCellFill<T> apply(final Sudoku<T> sudoku) {
        for (final Cell<T> cell : sudoku.getCells()) {
            if (cell.getNumberOfCandidates() == 1) {
                return new DeterministicCellFill<>(cell, cell.getFirstCandidateValue());
            }
        }
        return null;
    }
}
