package is.svartifoss.sudoku.solver;

import is.svartifoss.sudoku.model.Sudoku;

import java.util.Optional;

public class Result<T> {

    private final Sudoku<T> sudoku;

    private final long numberOfNonDeterministicCellFills;

    Result(final Sudoku<T> sudoku, final long numberOfNonDeterministicCellFills) {
        this.sudoku = sudoku;
        this.numberOfNonDeterministicCellFills = numberOfNonDeterministicCellFills;
    }

    Result(final long numberOfNonDeterministicCellFills) {
        this(null, numberOfNonDeterministicCellFills);
    }

    public Optional<Sudoku<T>> grid() {
        return Optional.ofNullable(sudoku);
    }

    public long getNumberOfNonDeterministicCellFills() {
        return numberOfNonDeterministicCellFills;
    }
}
