package is.svartifoss.sudoku;

import is.svartifoss.sudoku.format.DefaultStyle;
import is.svartifoss.sudoku.format.Formatter;
import is.svartifoss.sudoku.model.Cell;
import is.svartifoss.sudoku.model.Grid;
import is.svartifoss.sudoku.solver.Result;
import is.svartifoss.sudoku.solver.Solver;
import is.svartifoss.sudoku.solver.oracle.CellWithMinimumNumberOfCandidatesOracle;
import is.svartifoss.sudoku.solver.strategy.FirstMatchCompositeStrategy;
import is.svartifoss.sudoku.solver.strategy.HiddenSingleStrategy;
import is.svartifoss.sudoku.solver.strategy.NakedSingleStrategy;
import is.svartifoss.sudoku.solver.strategy.Strategy;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class SolverTest {
    @Test
    public void test() throws URISyntaxException, IOException {
        final Formatter formatter = new Formatter(DefaultStyle.CLASSIC);
        final Strategy strategy = new FirstMatchCompositeStrategy(Arrays.asList(new NakedSingleStrategy(), new HiddenSingleStrategy()));
        final Solver solver = new Solver(new CellWithMinimumNumberOfCandidatesOracle(), strategy);

        final List<String> lines = Files.readAllLines(Paths.get(getClass().getResource("/sudoku17").toURI()));
        System.out.println("total: " + lines.size());

        final long startTime = System.currentTimeMillis();
        for (int index = 0; index < lines.size(); index++) {
            if (index % 1000 == 0) {
                final float speed = 1000 * (float) index / (System.currentTimeMillis() - startTime);
                System.out.printf("%f sudokus per second%n", speed);
            }
            Result<Integer> result = solver.solve(Grid.valueOf(lines.get(index)));
            assertThat(result.grid()).isPresent();
//            System.out.println(formatter.format(result.grid().get()));
//            System.out.printf("number of trials: %d%n", result.getNumberOfNonDeterministicCellFills());
        }
    }

    @Test
    public void emptyGridIsSolvableByOracle() {
        final Formatter formatter = new Formatter(DefaultStyle.CLASSIC);
        final Solver solver = new Solver(new CellWithMinimumNumberOfCandidatesOracle(), new FirstMatchCompositeStrategy(Collections.emptyList()));

        final Result<Integer> result = solver.solve(new Grid<Integer>(IntStream.rangeClosed(1, 9).boxed().collect(toSet())));

        assertThat(result.grid()).isPresent();
        // assertThat(result.grid()) // 123456789456789123789123456231674895875912364694538217317265948542897631968341572
        assertThat(result.getNumberOfNonDeterministicCellFills()).isEqualTo(81);
        System.out.println(formatter.format(result.grid().get()));
        System.out.printf("number of trials: %d%n", result.getNumberOfNonDeterministicCellFills());
        System.out.println(result.grid().get().getCells().stream().map(Cell::getValue).map(Object::toString).collect(joining()));
    }

    @Test
    public void emptyGridOfOrder2IsSolvableByOracle() {
        final Formatter formatter = new Formatter(DefaultStyle.CLASSIC);
        final Solver solver = new Solver(new CellWithMinimumNumberOfCandidatesOracle(), new FirstMatchCompositeStrategy(Collections.emptyList()));

        final Result<Integer> result = solver.solve(new Grid<Integer>(IntStream.rangeClosed(1, 4).boxed().collect(toSet())));

        assertThat(result.grid()).isPresent();
        // assertThat(result.grid()) // 123456789456789123789123456231674895875912364694538217317265948542897631968341572
        assertThat(result.getNumberOfNonDeterministicCellFills()).isEqualTo(16);
        System.out.println(formatter.format(result.grid().get()));
        System.out.printf("number of trials: %d%n", result.getNumberOfNonDeterministicCellFills());
        System.out.println(result.grid().get().getCells().stream().map(Cell::getValue).map(Object::toString).collect(joining()));
    }
}