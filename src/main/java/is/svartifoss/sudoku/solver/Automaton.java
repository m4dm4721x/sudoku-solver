package is.svartifoss.sudoku.solver;

import is.svartifoss.sudoku.model.CellFill;
import is.svartifoss.sudoku.model.Sudoku;
import is.svartifoss.sudoku.oracle.Oracle;
import is.svartifoss.sudoku.strategy.Strategy;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;

class Automaton<T> {

    private final Oracle oracle;

    private final List<Strategy> strategies;

    private final Stack<CellFill<T>> trialCellFills;

    private Sudoku<T> sudoku;

    private long numberOfTrials;

    private boolean done;

    public Automaton(final Oracle oracle, final List<Strategy> strategies, final Sudoku<T> sudoku) {
        this.oracle = oracle;
        this.strategies = strategies;
        this.trialCellFills = new Stack<>();
        this.sudoku = new Sudoku<>(sudoku);
    }

    public Sudoku<T> getSudoku() {
        return sudoku;
    }

    public long getNumberOfTrials() {
        return numberOfTrials;
    }

    public boolean isDone() {
        return done;
    }

    public void advance() {
        if (sudoku.isSolved()) {
            done = true;
        } else if (sudoku.isFeasible()) {
            fillNextCell();
        } else if (canBacktrack()) {
            backtrack();
        } else {
            done = true;
        }
    }

    private void fillNextCell() {
        final CellFill<T> cellFill = getNextCellFill();
        if (cellFill.isTrial()) {
            trialCellFills.push(cellFill);
            numberOfTrials += 1;
        }
        sudoku = cellFill.apply();
    }

    private CellFill<T> getNextCellFill() {
        return strategies.stream()
                .map(strategy -> strategy.apply(sudoku))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(oracle.apply(sudoku));
    }

    private void backtrack() {
        sudoku = trialCellFills.pop().revert();
    }

    private boolean canBacktrack() {
        return !trialCellFills.isEmpty();
    }
}