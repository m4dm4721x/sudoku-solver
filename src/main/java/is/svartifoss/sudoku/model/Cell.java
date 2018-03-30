package is.svartifoss.sudoku.model;

import java.util.LinkedHashSet;
import java.util.Set;

public class Cell<T> {

    private final Sudoku<T> sudoku;

    private Set<T> candidateValues;

    private final int index;

    private final int rowIndex;

    private final int columnIndex;

    private T value;

    Cell(final Sudoku<T> sudoku, int index, final int rowIndex, final int columnIndex) {
        this.sudoku = sudoku;
        this.candidateValues = new LinkedHashSet<>(sudoku.getTargetSet());
        this.index = index;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    Cell(final Sudoku<T> sudoku, final Cell<T> cell) {
        this.sudoku = sudoku;
        this.value = cell.value;
        this.candidateValues = new LinkedHashSet<>(cell.candidateValues);
        this.index = cell.index;
        this.rowIndex = cell.rowIndex;
        this.columnIndex = cell.columnIndex;
    }

    boolean isFeasible() {
        return value != null || !candidateValues.isEmpty();
    }

    public boolean isSolved() {
        return value != null;
    }

    public T getValue() {
        return value;
    }

    void setValue(final T value) {
        this.value = value;
        candidateValues.clear();
        sudoku.updatePeers(this);
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

    public boolean hasCandidateValue(final T value) {
        return candidateValues.contains(value);
    }

    public T getFirstCandidateValue() {
        return candidateValues.isEmpty() ? null : candidateValues.iterator().next();
    }

    public int getNumberOfCandidates() {
        return candidateValues.size();
    }

    public Sudoku<T> getSudoku() {
        return sudoku;
    }

    int getIndex() {
        return index;
    }
}
