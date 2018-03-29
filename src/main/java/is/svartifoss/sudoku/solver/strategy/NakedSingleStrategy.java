package is.svartifoss.sudoku.solver.strategy;

import is.svartifoss.sudoku.model.Cell;
import is.svartifoss.sudoku.model.Grid;
import is.svartifoss.sudoku.model.CellFill;
import is.svartifoss.sudoku.model.DeterministicCellFill;

import java.util.Optional;

public class NakedSingleStrategy implements Strategy {
    @Override
    public <T> Optional<CellFill<T>> apply(final Grid<T> grid) {
        for (final Cell<T> cell : grid.getCells()) {
            final Optional<T> uniqueCandidateValue = cell.findUniqueCandidateValue();
            if (uniqueCandidateValue.isPresent()) {
                return Optional.of(new DeterministicCellFill<>(cell, uniqueCandidateValue.get()));
            }
        }
        return Optional.empty();
    }
}
