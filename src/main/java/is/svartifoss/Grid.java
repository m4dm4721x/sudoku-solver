package is.svartifoss;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Grid {

    private final Cell[] cells;

    private final Cell[][] rows;

    private final Cell[][] columns;

    private final Cell[][] blocks;

    private final Set<Cell>[][] peers;

 /*  private final List<Region> rows;

    private final List<Region> columns;

    private final List<Region> blocks;*/

    public static Grid valueOf(final String string) {
        if (string.length() != 81)
            throw new IllegalArgumentException();
        final Grid grid = new Grid();
        for (int i = 0; i < 81; i++) {
            final char digit = string.charAt(i);
            if (digit != '0') {
                grid.cells[i].setValue(digit - '0');
            }
        }
        return grid;
    }

    public Grid() {
        cells = new Cell[81];
        for (int cellIndex = 0; cellIndex < 81; cellIndex++) {
            cells[cellIndex] = new Cell(cellIndex / 9, cellIndex % 9);
        }
        rows = new Cell[9][9];
        columns = new Cell[9][9];
        blocks = new Cell[9][9];
        for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                rows[rowIndex][columnIndex] = columns[columnIndex][rowIndex] = blocks[columnIndex / 3 + rowIndex / 3 * 3][columnIndex % 3 + rowIndex % 3 * 3] = cells[9 * rowIndex + columnIndex];
            }
        }
        peers = new Set[9][9];
        for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                peers[rowIndex][columnIndex] = new LinkedHashSet<>();
                for (int k = 0; k < 9; k++) {
                    if (k != columnIndex)
                        peers[rowIndex][columnIndex].add(rows[rowIndex][k]);
                }
                for (int k = 0; k < 9; k++) {
                    if (k != rowIndex)
                        peers[rowIndex][columnIndex].add(columns[columnIndex][k]);
                }
                final int blockIndex = columnIndex / 3 + rowIndex / 3 * 3;
                final int cellIndex = columnIndex % 3 + rowIndex % 3 * 3;
                for (int k = 0; k < 9; k++) {
                    if (k != cellIndex)
                        peers[rowIndex][columnIndex].add(blocks[blockIndex][k]);
                }
            }
        }
    }

    public Grid copy(final Cell cell, final int candidateValue) {
        final Grid copy = new Grid();
        for (int cellIndex = 0; cellIndex < 81; cellIndex++) {
            if (this.cells[cellIndex].value != null) {
                copy.cells[cellIndex].setValue(this.cells[cellIndex].value);
            }
        }
        final int candidateCellIndex = cell.rowIndex * 9 + cell.columnIndex;
        copy.cells[candidateCellIndex].setValue(candidateValue);
        return copy;
    }

    public Cell getCell(final int rowIndex, final int columnIndex) {
        return rows[rowIndex][columnIndex];
    }

    private void updatePeerCandidates(final int row, final int column) {
        final int value = rows[row][column].value;
        peers[row][column].forEach(cell -> cell.removeCandidate(value));
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                stringBuilder.append(getCell(i, j).value().map(Object::toString).orElse("."));
                if ((j % 3) == 2)
                    stringBuilder.append("  ");
            }
            stringBuilder.append(System.lineSeparator());
            if ((i % 3) == 2)
                stringBuilder.append(System.lineSeparator());
        }
        /*for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                stringBuilder.append("(").append(rowIndex).append(",").append(columnIndex).append(") candidates = ");
                stringBuilder.append(getCell(rowIndex, columnIndex).getCandidates());
                stringBuilder.append(System.lineSeparator());
            }
        }*/
        return stringBuilder.toString();
    }

    public Cell[] getCells() {
        return cells;
    }

    public Cell[][] getRows() {
        return rows;
    }

    public Cell[][] getColumns() {
        return columns;
    }

    public Cell[][] getBlocks() {
        return blocks;
    }

    public boolean isSolved() {
        return Arrays.stream(rows).allMatch(Grid::isSolvedRegion)
                && Arrays.stream(columns).allMatch(Grid::isSolvedRegion)
                && Arrays.stream(blocks).allMatch(Grid::isSolvedRegion);
    }

    public boolean isSolvable() {
        return Arrays.stream(cells).allMatch(cell -> cell.value != null || !cell.getCandidates().isEmpty());
    }

    private static boolean isSolvedRegion(final Cell[] cells) {
        return Arrays.stream(cells).allMatch(cell -> cell.value != null && cell.getCandidates().isEmpty());
    }

/*
    public Grid(final Grid grid, final int candidateIndex, final int candidate) {
        cells = new ArrayList<>(81);
        for (int index = 0; index < 81; index++) {
            cells.set(index, new Cell(grid.cells.get(index)));
        }
        *//*rows = new Cell[9][9];
        columns = new Cell[9][9];
        blocks = new Cell[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                rows[i][j] = columns[j][i] = blocks[j / 3 + i / 3 * 3][j % 3 + i % 3 * 3] = cells[9 * i + j];
            }
        }*//*
        cells[candidateIndex].setValue(candidate);
    }

    public Cell[] getCells() {
        return cells;
    }

    public List<Region> rows() {
        return rows;
    }

    public List<Region> columns() {
        return columns;
    }

    public List<Region> blocks() {
        return blocks;
    }

    public boolean isSolved() {

    }

    public static boolean isSolved(Cell[] region) {
        return Arrays.stream(region).allMatch(cell -> cell.value().isPresent()) && Arrays.stream(region).mapToInt(cell -> cell.value().get()).sum() == 45;
    }

    public Set<Grid> getBrutePossibilites() {
        final Set<Grid> brutePossibilites = new HashSet<>();
        Map<Integer, Cell> c = IntStream.range(0, 81).filter(i -> !cells[i].value().isPresent()).boxed().collect(Collectors.toMap(Function.identity(), i -> cells[i]));
        if (!c.isEmpty()) {
            Map.Entry<Integer, Cell> i = c.entrySet().stream().sorted((a, b) -> a.getValue().getCandidates().size() - b.getValue().getCandidates().size()).findFirst().get();
            for (int candidate : i.getValue().getCandidates()) {
                brutePossibilites.add(new Grid(this, i.getKey(), candidate));
            }
        }
        return brutePossibilites;
    }

    public Cell getCell(final int i, final int j) {
        return cells[9 * i + j];
    }

    public boolean isSolvable() {
        return Arrays.stream(cells).map(Cell::getCandidates).noneMatch(Set::isEmpty);
    }*/

    public class Cell {

        private final int rowIndex;

        private final int columnIndex;

        private Integer value;

        private final List<Integer> candidates;

        public Cell(final int rowIndex, final int columnIndex) {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
            this.candidates = IntStream.rangeClosed(1, 9).boxed().collect(Collectors.toList());
        }

        public Optional<Integer> value() {
            return Optional.ofNullable(value);
        }

        public void setValue(final int value) {
            this.value = value;
            candidates.clear();
            updatePeerCandidates(rowIndex, columnIndex);
        }

        public List<Integer> getCandidates() {
            return Collections.unmodifiableList(candidates);
        }

        public boolean removeCandidates(final Set<Integer> values) {
            return candidates.removeAll(values);
        }

        public void removeCandidate(final int value) {
            candidates.removeIf(i -> i == value);
            /*if (candidates.size() == 1) {
                setValue(candidates.get(0));
            }*/
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Cell cell = (Cell) o;
            return rowIndex == cell.rowIndex &&
                    columnIndex == cell.columnIndex;
        }

        @Override
        public int hashCode() {
            return Objects.hash(rowIndex, columnIndex);
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", rowIndex, columnIndex);
        }

        public int getIndex() {
            return 9 * rowIndex + columnIndex;
        }
    }

    public boolean mayBeCompletedTo(final Grid grid) {
        if (!grid.isSolved()) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (getCell(i, j).value != null)
                    if (!grid.getCell(i, j).value.equals(getCell(i, j).value)) {
                        return false;
                    }
            }
        }
        return true;
    }
}
