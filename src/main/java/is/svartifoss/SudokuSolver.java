package is.svartifoss;

import java.util.ArrayList;
import java.util.List;

public class SudokuSolver {

    private List<Strategy> strategies;

    private Oracle oracle;

    public static void main(String[] args) {
        final Grid grid = Grid.valueOf("030000010000200500000000400700006000000010030090000000001063000000400007800000200");
        final SudokuSolver sudokuSolver = new SudokuSolver();
        final Grid processedGrid = sudokuSolver.solve(grid);
        if (processedGrid.isSolved()) {
            System.out.println(processedGrid);
        } else {
            System.out.println("unsolvable");
        }
    }

    public SudokuSolver() {
        strategies = new ArrayList<>();
        strategies.add(new NakedSingleStrategy());
        strategies.add(new HiddenSingleStrategy());
    }

    public Grid solve(final Grid initialGrid) {
        oracle = new Oracle(initialGrid);
        Grid grid = initialGrid;
        while (!grid.isSolved()) {
            for (int index = 0; index < strategies.size(); index++) {
                if (strategies.get(index).apply(grid))
                    index = -1;
            }
            if (!grid.isSolved()) {
                grid = oracle.predict();
                if (grid == initialGrid)
                    return initialGrid;
            }
        }
        return grid;
    }

/*
    private final char[] field;

    public SudokuSolver(final char[] field) {
        this.field = field;
    }

    public SudokuSolver(final String filename) {
        this.field = new char[81];
        try {
            final String string = new String(Files.readAllBytes(Paths.get(getClass().getResource(filename).toURI())), StandardCharsets.UTF_8)
                    .replaceAll("\\s", "");
            assert string.matches("[0-9]{81}");
            for (int i = 0; i < 81; i++) {
                field[i] = string.charAt(i);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean solve() {
        return solve(0);
    }

    private boolean solve(final int index) {
        if (index < 81) {
            if (field[index] != '0') {
                return solve(index + 1);
            } else {
                for (char i = '1'; i <= '9'; i++) {
                    if (isValidChoice(index, i)) {
                        field[index] = i;
                        if (solve(index + 1))
                            return true;
                        field[index] = '0';
                    }
                }
                return false;
            }
        } else {
            return true;
        }
    }

    private boolean isValidChoice(final int index, final char digit) {
        int row = index / 9;
        int column = index % 9;
        for (int i = 0; i < 9; i++) {
            if (equals(row, i, digit))
                return false;
            if (equals(i, column, digit))
                return false;
            if (equals(row / 3 * 3 + i / 3, column / 3 * 3 + i % 3, digit))
                return false;
        }
        return true;
    }

    private boolean equals(final int row, final int column, final char digit) {
        return field[row * 9 + column] == digit;
    }

    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                stringBuilder.append(field[i * 9 + j]);
            }
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }*/
}
