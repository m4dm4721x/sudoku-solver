package is.svartifoss;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Stack;

public class Oracle {

    private static String s = "432659718167248593985137426714396852528714639396582174271863945659421387843975261";

    private static Grid solution = Grid.valueOf(s);
    
    private final Grid initialGrid;

    private final Stack<Trial> trials;

    public Oracle(final Grid initialGrid) {
        this.initialGrid = initialGrid;
        trials = new Stack<>();
    }

    Grid predict() {
        Grid current = trials.isEmpty() ? initialGrid : trials.peek().grid;
        if (current.isSolvable()) {
            Optional<Grid.Cell> c = Arrays.stream(current.getCells())
                    .filter(cell -> !cell.getCandidates().isEmpty())
                    .min(Comparator.comparingInt(a -> a.getCandidates().size()));
            if (!c.isPresent())
                throw new IllegalStateException();
            Integer candidateValue = c.get().getCandidates().get(0);
            Grid trialGrid = current.copy(c.get(), candidateValue);
            if (trialGrid.isSolvable()) {
                trials.push(new Trial(trialGrid, current, c.get().getIndex(), candidateValue));
                return trialGrid;
            } else {
                current.getCells()[c.get().getIndex()].removeCandidate(candidateValue);
                return predict();
            }
        } else {
            Trial trial = trials.pop();
            trial.parentGrid.getCells()[trial.cellIndex].removeCandidate(trial.value);
            return predict();
        }
    }

    private static class Trial {
        private final Grid grid;

        private final Grid parentGrid;

        private final int cellIndex;

        private final int value;

        private Trial(final Grid grid, final Grid parentGrid, final int cellIndex, final int value) {
            this.grid = grid;
            this.parentGrid = parentGrid;
            this.cellIndex = cellIndex;
            this.value = value;
        }
    }
}
