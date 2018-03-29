package is.svartifoss.sudoku.model;

public class DeterministicCellFill<T> implements CellFill<T> {

    private final Cell<T> cell;

    private final T value;

    public DeterministicCellFill(final Cell<T> cell, final T value) {
        this.cell = cell;
        this.value = value;
    }

    @Override
    public Grid<T> apply() {
        return new Grid<>(cell.getGrid(), cell, value);
    }

    @Override
    public Grid<T> revert() {
        return cell.getGrid();
    }

    @Override
    public boolean isNonDeterministic() {
        return false;
    }
}
