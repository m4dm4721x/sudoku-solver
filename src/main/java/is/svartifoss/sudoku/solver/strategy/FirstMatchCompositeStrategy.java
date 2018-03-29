package is.svartifoss.sudoku.solver.strategy;

import is.svartifoss.sudoku.model.CellFill;
import is.svartifoss.sudoku.model.Grid;

import java.util.List;
import java.util.Optional;

public final class FirstMatchCompositeStrategy implements Strategy {

    private final List<Strategy> strategies;

    public FirstMatchCompositeStrategy(final List<Strategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public <T> Optional<CellFill<T>> apply(final Grid<T> grid) {
        return strategies.stream().map(grid::accept).filter(Optional::isPresent).findFirst().map(Optional::get);
    }
}
