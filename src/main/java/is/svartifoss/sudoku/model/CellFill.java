package is.svartifoss.sudoku.model;

public interface CellFill<T> {
    Sudoku<T> apply();

    Sudoku<T> revert();

    boolean isTrial();
}
