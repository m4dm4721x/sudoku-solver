package is.svartifoss.sudoku.model;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;

public class Sudoku<T> {

    private final Cell<T>[] cells;

    private final Cell<T>[][] rows;

    private final Cell<T>[][] columns;

    private final Cell<T>[][] blocks;

    private final Cell<T>[][] peers;

    private final int sideLength;

    private final int order;

    private final Set<T> targetSet;

    public Sudoku(final Set<T> targetSet) {

        this.targetSet = Collections.unmodifiableSet(new HashSet<>(targetSet));

        order = getOrder(targetSet);
        sideLength = order * order;
        final int cellCount = sideLength * sideLength;

        cells = new Cell[cellCount];
        for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
            cells[cellIndex] = new Cell<>(this, cellIndex, cellIndex / sideLength, cellIndex % sideLength);
        }

        rows = new Cell[sideLength][sideLength];
        for (int rowIndex = 0; rowIndex < sideLength; rowIndex++) {
            for (int columnIndex = 0; columnIndex < sideLength; columnIndex++) {
                rows[rowIndex][columnIndex] = cells[sideLength * rowIndex + columnIndex];
            }
        }

        columns = new Cell[sideLength][sideLength];
        for (int columnIndex = 0; columnIndex < sideLength; columnIndex++) {
            for (int rowIndex = 0; rowIndex < sideLength; rowIndex++) {
                columns[columnIndex][rowIndex] = cells[sideLength * rowIndex + columnIndex];
            }
        }

        blocks = new Cell[sideLength][sideLength];
        for (int blockIndex = 0; blockIndex < sideLength; blockIndex++) {
            for (int cellIndex = 0; cellIndex < sideLength; cellIndex++) {
                final int rowIndex = blockIndex / order * order + cellIndex / order;
                final int columnIndex = blockIndex % order * order + cellIndex % order;
                blocks[blockIndex][cellIndex] = cells[sideLength * rowIndex + columnIndex];
            }
        }

        peers = new Cell[sideLength*sideLength][3 * (sideLength - 1) - 2 * (order - 1)];
        int cellPeerIndex = 0;
        for (final Cell<T> cell : cells) {
            final int rowIndex = cell.getRowIndex();
            final int columnIndex = cell.getColumnIndex();
            int peerIndex = 0;
            for (int k = 0; k < sideLength; k++) {
                if (k != columnIndex)
                    peers[cellPeerIndex][peerIndex++] = rows[rowIndex][k];
            }
            for (int k = 0; k < sideLength; k++) {
                if (k != rowIndex)
                    peers[cellPeerIndex][peerIndex++] = columns[columnIndex][k];
            }
            final int blockIndex = columnIndex / order + rowIndex / order * order;
            final int cellIndex = columnIndex % order + rowIndex % order * order;
            for (int k = 0; k < sideLength; k++) {
                final Cell<T> peerCell = blocks[blockIndex][k];
                if (k != cellIndex && peerCell.getRowIndex() != rowIndex && peerCell.getColumnIndex() != columnIndex) {
                    peers[cellPeerIndex][peerIndex++] = peerCell;
                }
            }
            cellPeerIndex += 1;
        }
    }

    public Sudoku(final Sudoku<T> sudoku) {
        this.targetSet = sudoku.targetSet;

        order = getOrder(targetSet);
        sideLength = order * order;
        final int cellCount = sideLength * sideLength;

        cells = new Cell[cellCount];
        for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
            cells[cellIndex] = new Cell<>(this, sudoku.cells[cellIndex]);
        }

        rows = new Cell[sideLength][sideLength];
        for (int rowIndex = 0; rowIndex < sideLength; rowIndex++) {
            for (int columnIndex = 0; columnIndex < sideLength; columnIndex++) {
                rows[rowIndex][columnIndex] = cells[sideLength * rowIndex + columnIndex];
            }
        }

        columns = new Cell[sideLength][sideLength];
        for (int columnIndex = 0; columnIndex < sideLength; columnIndex++) {
            for (int rowIndex = 0; rowIndex < sideLength; rowIndex++) {
                columns[columnIndex][rowIndex] = cells[sideLength * rowIndex + columnIndex];
            }
        }

        blocks = new Cell[sideLength][sideLength];
        for (int blockIndex = 0; blockIndex < sideLength; blockIndex++) {
            for (int cellIndex = 0; cellIndex < sideLength; cellIndex++) {
                final int rowIndex = blockIndex / order * order + cellIndex / order;
                final int columnIndex = blockIndex % order * order + cellIndex % order;
                blocks[blockIndex][cellIndex] = cells[sideLength * rowIndex + columnIndex];
            }
        }

        peers = new Cell[sideLength*sideLength][3 * (sideLength - 1) - 2 * (order - 1)];
        int cellPeerIndex = 0;
        for (final Cell<T> cell : cells) {
            final int rowIndex = cell.getRowIndex();
            final int columnIndex = cell.getColumnIndex();
            int peerIndex = 0;
            for (int k = 0; k < sideLength; k++) {
                if (k != columnIndex)
                    peers[cellPeerIndex][peerIndex++] = rows[rowIndex][k];
            }
            for (int k = 0; k < sideLength; k++) {
                if (k != rowIndex)
                    peers[cellPeerIndex][peerIndex++] = columns[columnIndex][k];
            }
            final int blockIndex = columnIndex / order + rowIndex / order * order;
            final int cellIndex = columnIndex % order + rowIndex % order * order;
            for (int k = 0; k < sideLength; k++) {
                final Cell<T> peerCell = blocks[blockIndex][k];
                if (k != cellIndex && peerCell.getRowIndex() != rowIndex && peerCell.getColumnIndex() != columnIndex) {
                    peers[cellPeerIndex][peerIndex++] = peerCell;
                }
            }
            cellPeerIndex += 1;
        }
    }

    public Sudoku(final Sudoku<T> sudoku, final Cell<T> cell, final T t) {
        this(sudoku);
        cells[cell.getIndex()].setValue(t);
    }

    private int getOrder(final Set<T> targetSet) {
        final int order = (int) Math.sqrt(targetSet.size());
        if (order * order != targetSet.size())
            throw new IllegalArgumentException("Cardinality of target set is not a perfect square!");
        return order;
    }

    void updatePeers(final Cell<T> cell) {
        Arrays.stream(peers[cell.getIndex()]).forEach(peerCell -> peerCell.removeCandidateValue(cell.getValue()));
    }

    public Cell<T>[] getCells() {
        return cells;
    }

    public int getSideLength() {
        return sideLength;
    }

    public int getOrder() {
        return order;
    }

    public Set<T> getTargetSet() {
        return targetSet;
    }

    public static Sudoku<Integer> valueOf(final String string) {
        if (!string.matches("\\d{81}"))
            throw new IllegalArgumentException();
        final Sudoku<Integer> sudoku = new Sudoku<>(IntStream.rangeClosed(1, 9).boxed().collect(toSet()));
        for (int i = 0; i < 81; i++) {
            final char digit = string.charAt(i);
            if (digit != '0') {
                sudoku.cells[i].setValue(digit - '0');
            }
        }
        return sudoku;
    }

    public boolean isFeasible() {
        for (int cellIndex = 0; cellIndex < cells.length; cellIndex++) {
            if (!cells[cellIndex].isFeasible())
                return false;
        }
        return true;
    }

    public boolean isSolved() {
        for (int cellIndex = 0; cellIndex < cells.length; cellIndex++) {
            if (!cells[cellIndex].isSolved())
                return false;
        }
        return true;
    }

    public Cell<T>[][] getRows() {
        return rows;
    }

    public Cell<T>[][] getColumns() {
        return columns;
    }

    public Cell<T>[][] getBlocks() {
        return blocks;
    }
}
