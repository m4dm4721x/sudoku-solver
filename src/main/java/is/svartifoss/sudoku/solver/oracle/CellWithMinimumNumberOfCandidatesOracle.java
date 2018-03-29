package is.svartifoss.sudoku.solver.oracle;

import is.svartifoss.sudoku.model.Cell;
import is.svartifoss.sudoku.model.Grid;
import is.svartifoss.sudoku.model.NonDeterministicCellFill;

import java.util.Comparator;

public class CellWithMinimumNumberOfCandidatesOracle implements Oracle {
    @Override
    public <T> NonDeterministicCellFill<T> apply(final Grid<T> grid) {
        final Cell<T> trialCell = grid.getCells().stream()
                .filter(cell -> cell.getValue() == null)
                .min(Comparator.comparingInt(Cell::getNumberOfCandidates)).orElseThrow(
                        () -> new IllegalStateException("Grid is not feasible!")
                );
        return new NonDeterministicCellFill<>(trialCell, trialCell.getFirstCandidateValue());
    }
}
