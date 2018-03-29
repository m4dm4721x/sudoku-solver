package is.svartifoss.sudoku.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Region<T> implements Solvable {

    private final List<Cell<T>> cells;

    private final Set<T> targetSet;

    public Region(final List<Cell<T>> cells, final Set<T> targetSet) {
        this.cells = cells;
        this.targetSet = targetSet;
    }

    @Override
    public boolean isFeasible() {
        return cells.stream().allMatch(cell -> cell.isSolved() || cell.isFeasible());
    }

    @Override
    public boolean isSolved() {
        return cells.stream().allMatch(Cell::isSolved)
                && cells.stream().map(Cell::getValue).collect(Collectors.toSet()).equals(targetSet);
    }

    public Cell<T> get(final int index) {
        return cells.get(index);
    }

    public Set<T> getTargetSet() {
        return targetSet;
    }

    public List<Cell<T>> getCells() {
        return cells;
    }
}
