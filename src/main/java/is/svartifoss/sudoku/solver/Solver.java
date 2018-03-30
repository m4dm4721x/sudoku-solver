package is.svartifoss.sudoku.solver;

import is.svartifoss.sudoku.model.Sudoku;
import is.svartifoss.sudoku.oracle.Oracle;
import is.svartifoss.sudoku.strategy.Strategy;

import java.util.Collections;
import java.util.List;

public class Solver {

    private final Oracle oracle;

    private final List<Strategy> strategies;

    public Solver(final Oracle oracle) {
        this(oracle, Collections.emptyList());
    }

    public Solver(final Oracle oracle, final List<Strategy> strategies) {
        this.oracle = oracle;
        this.strategies = strategies;
    }

    public <T> Result<T> solve(final Sudoku<T> sudoku) {
        final Automaton<T> automaton = new Automaton<>(oracle, strategies, sudoku);
        while (!automaton.isDone()) {
            automaton.advance();
        }
        return new Result<>(automaton.getSudoku(), automaton.getNumberOfTrials());
    }
}