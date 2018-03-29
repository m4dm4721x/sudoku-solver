package is.svartifoss.sudoku.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class Cell<T> implements Solvable {

    private final Grid<T> grid;

    private Set<T> candidateValues;

    private final int rowIndex;

    private final int columnIndex;

    private T value;

    public Cell(final Grid<T> grid, final int rowIndex, final int columnIndex) {
        this.grid = grid;
        this.candidateValues = new LinkedHashSet<>(grid.getTargetSet());
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    public Cell(final Grid<T> grid, final Cell<T> cell) {
        this.grid = grid;
        this.value = cell.value;
        this.candidateValues = new LinkedHashSet<>(cell.candidateValues);
        this.rowIndex = cell.getRowIndex();
        this.columnIndex = cell.getColumnIndex();
    }

    @Override
    public boolean isFeasible() {
        return !candidateValues.isEmpty();
    }

    @Override
    public boolean isSolved() {
        return value != null;
    }

    public T getValue() {
        return value;
    }

    void setValue(final T value) {
        this.value = value;
        candidateValues.clear();
        grid.updatePeers(this);
    }

    int getRowIndex() {
        return rowIndex;
    }

    int getColumnIndex() {
        return columnIndex;
    }

    void removeCandidateValue(final T value) {
        candidateValues.remove(value);
    }

    public Optional<T> findUniqueCandidateValue() {
        return (candidateValues.size() == 1) ? Optional.of(candidateValues.iterator().next()) : Optional.empty();
    }

    public boolean hasCandidateValue(final T value) {
        return candidateValues.contains(value);
    }

    public T getFirstCandidateValue() {
        return candidateValues.isEmpty() ? null : candidateValues.iterator().next();
    }

    public int getNumberOfCandidates() {
        return candidateValues.size();
    }

    public Grid<T> getGrid() {
        return grid;
    }
}
