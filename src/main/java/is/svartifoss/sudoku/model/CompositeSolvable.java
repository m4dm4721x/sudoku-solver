package is.svartifoss.sudoku.model;

import java.util.stream.Stream;

public abstract class CompositeSolvable<T extends Solvable> implements Solvable {

    protected abstract Stream<T> getChildren();

    @Override
    public boolean isFeasible() {
        return getChildren().allMatch(Solvable::isFeasible);
    }

    @Override
    public boolean isSolved() {
        return getChildren().allMatch(Solvable::isSolved);
    }
}
