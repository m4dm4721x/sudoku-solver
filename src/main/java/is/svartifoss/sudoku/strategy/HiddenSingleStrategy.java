package is.svartifoss.sudoku.strategy;

import is.svartifoss.sudoku.model.Cell;
import is.svartifoss.sudoku.model.DeterministicCellFill;
import is.svartifoss.sudoku.model.Sudoku;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class HiddenSingleStrategy implements Strategy {

    @Override
    public <T> DeterministicCellFill<T> apply(final Sudoku<T> sudoku) {
        DeterministicCellFill<T> result = apply(sudoku.getRows(), sudoku.getTargetSet());
        if (result != null)
            return result;
        result = apply(sudoku.getColumns(), sudoku.getTargetSet());
        if (result != null)
            return result;
        return apply(sudoku.getBlocks(), sudoku.getTargetSet());
    }

    private <T> DeterministicCellFill<T> apply(final Cell<T>[][] regions, final Set<T> targetSet) {
        for (final Cell<T>[] region : regions) {
            Optional<Map.Entry<T, Set<Cell<T>>>> hiddenSingle = targetSet.stream()
                    .collect(toMap(
                            Function.identity(),
                            value -> Arrays.stream(region).filter(cell -> cell.hasCandidateValue(value)).collect(toSet())
                    ))
                    .entrySet().stream()
                    .filter(entry -> entry.getValue().size() == 1)
                    .findFirst();
            if (hiddenSingle.isPresent()) {
                return new DeterministicCellFill<>(hiddenSingle.get().getValue().iterator().next(), hiddenSingle.get().getKey());
            }
        }
        return null;
    }
}
