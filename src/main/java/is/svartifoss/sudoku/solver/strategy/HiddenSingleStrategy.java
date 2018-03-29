package is.svartifoss.sudoku.solver.strategy;

import is.svartifoss.sudoku.model.CellFill;
import is.svartifoss.sudoku.model.DeterministicCellFill;
import is.svartifoss.sudoku.model.Cell;
import is.svartifoss.sudoku.model.Cover;
import is.svartifoss.sudoku.model.Grid;
import is.svartifoss.sudoku.model.Region;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class HiddenSingleStrategy implements Strategy {
    @Override
    public <T> Optional<CellFill<T>> apply(final Grid<T> grid) {
        for (final Cover<T> cover : grid.getCovers()) {
            for (final Region<T> region : cover.getRegions()) {
                Optional<Map.Entry<T, Set<Cell<T>>>> hiddenSingle = region.getTargetSet().stream()
                        .collect(toMap(
                                Function.identity(),
                                value -> region.getCells().stream().filter(cell -> cell.hasCandidateValue(value)).collect(toSet())
                        ))
                        .entrySet().stream()
                        .filter(entry -> entry.getValue().size() == 1)
                        .findFirst();
                if (hiddenSingle.isPresent()) {
                    return Optional.of(new DeterministicCellFill<>(hiddenSingle.get().getValue().iterator().next(), hiddenSingle.get().getKey()));
                }
            }
        }
        return Optional.empty();
    }
}
