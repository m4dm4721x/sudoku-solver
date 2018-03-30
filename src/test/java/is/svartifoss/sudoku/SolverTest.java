package is.svartifoss.sudoku;

import is.svartifoss.sudoku.format.DefaultStyle;
import is.svartifoss.sudoku.format.Formatter;
import is.svartifoss.sudoku.model.Cell;
import is.svartifoss.sudoku.model.Sudoku;
import is.svartifoss.sudoku.oracle.CellWithMinimumNumberOfCandidatesOracle;
import is.svartifoss.sudoku.solver.Result;
import is.svartifoss.sudoku.solver.Solver;
import is.svartifoss.sudoku.strategy.HiddenSingleStrategy;
import is.svartifoss.sudoku.strategy.NakedSingleStrategy;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class SolverTest {
    @Test
    public void test() throws URISyntaxException, IOException {
        final Formatter formatter = new Formatter(DefaultStyle.CLASSIC);
        final Solver solver = new Solver(new CellWithMinimumNumberOfCandidatesOracle(), Arrays.asList(new NakedSingleStrategy(), new HiddenSingleStrategy()));

        final List<String> lines = Files.readAllLines(Paths.get(getClass().getResource("/sudoku17").toURI()));
        System.out.println("total: " + lines.size());

        final long startTime = System.currentTimeMillis();
        for (int index = 0; index < lines.size(); index++) {
            if (index % 1000 == 0) {
                final float speed = 1000 * (float) index / (System.currentTimeMillis() - startTime);
                System.out.printf("%f sudokus per second%n", speed);
            }
            Result<Integer> result = solver.solve(Sudoku.valueOf(lines.get(index)));
            assertThat(result.grid()).isPresent();
//            System.out.println(formatter.format(result.grid().get()));
//            System.out.printf("number of trials: %d%n", result.getNumberOfNonDeterministicCellFills());
        }
    }

    @Test
    public void emptyGridIsSolvableByOracle() {
        final Formatter formatter = new Formatter(DefaultStyle.CLASSIC);
        final Solver solver = new Solver(new CellWithMinimumNumberOfCandidatesOracle(), Collections.emptyList());

        final Result<Integer> result = solver.solve(new Sudoku<>(IntStream.rangeClosed(1, 9).boxed().collect(toSet())));

        assertThat(result.grid()).isPresent();
        // assertThat(result.grid()) // 123456789456789123789123456231674895875912364694538217317265948542897631968341572
        assertThat(result.getNumberOfNonDeterministicCellFills()).isEqualTo(81);
        System.out.println(formatter.format(result.grid().get()));
        System.out.printf("number of trials: %d%n", result.getNumberOfNonDeterministicCellFills());
        System.out.println(Arrays.stream(result.grid().get().getCells()).map(Cell::getValue).map(Object::toString).collect(joining()));
    }

    @Test
    public void emptyGridOfOrder2IsSolvableByOracle() {
        final Formatter formatter = new Formatter(DefaultStyle.DOUBLE);
        final Solver solver = new Solver(new CellWithMinimumNumberOfCandidatesOracle());

        final Result<String> result = solver.solve(new Sudoku<>(new LinkedHashSet<>(Arrays.asList("←", "→", "↑", "↓"))));

        assertThat(result.grid()).isPresent();
        // assertThat(result.grid()) // 123456789456789123789123456231674895875912364694538217317265948542897631968341572
        assertThat(result.getNumberOfNonDeterministicCellFills()).isEqualTo(16);
        System.out.println(formatter.format(result.grid().get()));
        System.out.printf("number of trials: %d%n", result.getNumberOfNonDeterministicCellFills());
        System.out.println(Arrays.stream(result.grid().get().getCells()).map(Cell::getValue).map(Object::toString).collect(joining()));
    }

    @Test
    public void emptyGridOfOrder4IsSolvableByOracle() {
        final Formatter formatter = new Formatter(DefaultStyle.ASCII);
        final Solver solver = new Solver(new CellWithMinimumNumberOfCandidatesOracle());

        final Result<String> result = solver.solve(new Sudoku<>(new LinkedHashSet<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p"))));

        assertThat(result.grid()).isPresent();
        // assertThat(result.grid()) // 123456789456789123789123456231674895875912364694538217317265948542897631968341572
        assertThat(result.getNumberOfNonDeterministicCellFills()).isEqualTo(282);
        System.out.println(formatter.format(result.grid().get()));
        System.out.printf("number of trials: %d%n", result.getNumberOfNonDeterministicCellFills());
        System.out.println(Arrays.stream(result.grid().get().getCells()).map(Cell::getValue).map(Object::toString).collect(joining()));
    }
}