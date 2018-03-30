package is.svartifoss.sudoku.oracle;

import is.svartifoss.sudoku.model.Cell;
import is.svartifoss.sudoku.model.Sudoku;
import is.svartifoss.sudoku.model.TrialCellFill;

import java.util.Arrays;
import java.util.Comparator;

public class CellWithMinimumNumberOfCandidatesOracle implements Oracle {
    @Override
    public <T> TrialCellFill<T> apply(final Sudoku<T> sudoku) {
        final Cell<T> trialCell = Arrays.stream(sudoku.getCells())
                .filter(cell -> !cell.isSolved())
                .min(Comparator.comparingInt(Cell::getNumberOfCandidates))
                .orElseThrow(() -> new IllegalStateException("Sudoku is not feasible!"));
        return new TrialCellFill<>(trialCell, trialCell.getFirstCandidateValue());
    }
}
