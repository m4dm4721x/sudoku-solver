package is.svartifoss;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class SudokuSolverTest {
    @Test
    public void test() throws URISyntaxException, IOException {
        List<String> lines = Files.readAllLines(Paths.get(getClass().getResource("/sudoku17").toURI()));
        System.out.println("total: " + lines.size());
        for (int i = 0; i < lines.size(); i++) {
            if (i % 1000 == 999) {
                System.out.println(i);
            }
            final String line = lines.get(i);
            SudokuSolver sudokuSolver = new SudokuSolver();
            Grid grid = sudokuSolver.solve(Grid.valueOf(line));
            if (grid.isSolved()) {
            } else {
                System.out.println("unsolved");
            }
        }
    }
    /*
    @Test
    public void test1() {
        SudokuSolver sudokuSolver = new SudokuSolver("/test1.txt");
        assertTrue(sudokuSolver.solve());
        System.out.println(sudokuSolver);
    }

    @Test
    public void test2() {
        SudokuSolver sudokuSolver = new SudokuSolver("/test2.txt");
        assertTrue(sudokuSolver.solve());
        System.out.println(sudokuSolver);
    }

    @Test
    public void test3() {
        SudokuSolver sudokuSolver = new SudokuSolver("/test3.txt");
        assertTrue(sudokuSolver.solve());
        System.out.println(sudokuSolver);
    }
    */
}