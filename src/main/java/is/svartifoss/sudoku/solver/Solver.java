package is.svartifoss.sudoku.solver;

import is.svartifoss.sudoku.model.CellFill;
import is.svartifoss.sudoku.model.Grid;
import is.svartifoss.sudoku.solver.oracle.Oracle;
import is.svartifoss.sudoku.solver.strategy.Strategy;

import java.util.LinkedList;

public class Solver {

    private final Oracle oracle;

    private final Strategy strategy;

    public Solver(final Oracle oracle, final Strategy strategy) {
        this.oracle = oracle;
        this.strategy = strategy;
    }

    public <T> Result<T> solve(final Grid<T> gridToBeSolved) {

        final LinkedList<CellFill<T>> cellFills = new LinkedList<>();
        int numberOfNonDeterministicCellFills = 0;

        Grid<T> grid = new Grid<>(gridToBeSolved);
        while (!grid.isSolved()) {
            if (grid.isFeasible()) {
                final CellFill<T> cellFill = strategy.apply(grid).orElse(oracle.apply(grid));
                if (cellFill.isNonDeterministic()) {
                    cellFills.addLast(cellFill);
                    numberOfNonDeterministicCellFills += 1;
                }
                grid = cellFill.apply();
            } else if (!cellFills.isEmpty()) {
                grid = cellFills.removeLast().revert();
            } else {
                return new Result<>(null, numberOfNonDeterministicCellFills);
            }
        }

        return new Result<>(grid, numberOfNonDeterministicCellFills);
    }
}
