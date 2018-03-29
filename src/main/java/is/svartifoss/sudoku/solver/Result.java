package is.svartifoss.sudoku.solver;

import is.svartifoss.sudoku.model.Grid;

import java.util.Optional;

public class Result<T> {

    private final Grid<T> grid;

    private final int numberOfNonDeterministicCellFills;

    Result(final Grid<T> grid, final int numberOfNonDeterministicCellFills) {
        this.grid = grid;
        this.numberOfNonDeterministicCellFills = numberOfNonDeterministicCellFills;
    }

    public Optional<Grid<T>> grid() {
        return Optional.ofNullable(grid);
    }

    public int getNumberOfNonDeterministicCellFills() {
        return numberOfNonDeterministicCellFills;
    }
}
