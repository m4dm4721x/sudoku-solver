package is.svartifoss.sudoku.model;

import is.svartifoss.sudoku.solver.strategy.Strategy;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class Grid<T> extends CompositeSolvable<Cover<T>> {

    private final List<Cell<T>> cells;

    private final List<Region<T>> rows;

    private final List<Region<T>> columns;

    private final List<Region<T>> blocks;

    private final Cover<T> rowCover;

    private final Cover<T> columnCover;

    private final Cover<T> blockCover;

    private final List<Cover<T>> covers;

    private final Map<Cell<T>, Set<Cell<T>>> peerMap;

    private final int sideLength;

    private final int order;

    private final Set<T> targetSet;

    public Grid(final Set<T> targetSet) {

        this.targetSet = Collections.unmodifiableSet(new HashSet<>(targetSet));

        order = getOrder(targetSet);
        sideLength = order * order;
        final int cellCount = sideLength * sideLength;

        cells = new ArrayList<>();
        for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
            cells.add(new Cell<>(this, cellIndex / sideLength, cellIndex % sideLength));
        }

        rows = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < sideLength; rowIndex++) {
            final List<Cell<T>> rowCells = new ArrayList<>();
            for (int columnIndex = 0; columnIndex < sideLength; columnIndex++) {
                rowCells.add(cells.get(sideLength * rowIndex + columnIndex));
            }
            rows.add(new Region<>(rowCells, targetSet));
        }

        columns = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < sideLength; columnIndex++) {
            final List<Cell<T>> columnCells = new ArrayList<>();
            for (int rowIndex = 0; rowIndex < sideLength; rowIndex++) {
                columnCells.add(cells.get(sideLength * rowIndex + columnIndex));
            }
            columns.add(new Region<>(columnCells, targetSet));
        }

        blocks = new ArrayList<>();
        for (int blockIndex = 0; blockIndex < sideLength; blockIndex++) {
            final List<Cell<T>> blockCells = new ArrayList<>();
            for (int cellIndex = 0; cellIndex < sideLength; cellIndex++) {
                final int rowIndex = blockIndex / order * order + cellIndex / order;
                final int columnIndex = blockIndex % order * order + cellIndex % order;
                blockCells.add(cells.get(sideLength * rowIndex + columnIndex));
            }
            blocks.add(new Region<>(blockCells, targetSet));
        }

        rowCover = new Cover<>(rows);
        columnCover = new Cover<>(columns);
        blockCover = new Cover<>(blocks);

        covers = Arrays.asList(rowCover, columnCover, blockCover);

        peerMap = new LinkedHashMap<>();
        for (final Cell<T> cell : cells) {
            final Set<Cell<T>> peers = new HashSet<>();

            final int rowIndex = cell.getRowIndex();
            final int columnIndex = cell.getColumnIndex();

            for (int k = 0; k < sideLength; k++) {
                if (k != columnIndex)
                    peers.add(rows.get(rowIndex).get(k));
            }
            for (int k = 0; k < sideLength; k++) {
                if (k != rowIndex)
                    peers.add(columns.get(columnIndex).get(k));
            }
            final int blockIndex = columnIndex / order + rowIndex / order * order;
            final int cellIndex = columnIndex % order + rowIndex % order * order;
            for (int k = 0; k < sideLength; k++) {
                if (k != cellIndex)
                    peers.add(blocks.get(blockIndex).get(k));
            }
            peerMap.put(cell, peers);
        }
    }

    public Grid(final Grid<T> grid) {
        this.targetSet = grid.targetSet;

        order = getOrder(targetSet);
        sideLength = order * order;
        final int cellCount = sideLength * sideLength;

        cells = new ArrayList<>();
        for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
            cells.add(new Cell<>(this, grid.getCells().get(cellIndex)));
        }

        rows = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < sideLength; rowIndex++) {
            final List<Cell<T>> rowCells = new ArrayList<>();
            for (int columnIndex = 0; columnIndex < sideLength; columnIndex++) {
                rowCells.add(cells.get(sideLength * rowIndex + columnIndex));
            }
            rows.add(new Region<>(rowCells, targetSet));
        }

        columns = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < sideLength; columnIndex++) {
            final List<Cell<T>> columnCells = new ArrayList<>();
            for (int rowIndex = 0; rowIndex < sideLength; rowIndex++) {
                columnCells.add(cells.get(sideLength * rowIndex + columnIndex));
            }
            columns.add(new Region<>(columnCells, targetSet));
        }

        blocks = new ArrayList<>();
        for (int blockIndex = 0; blockIndex < sideLength; blockIndex++) {
            final List<Cell<T>> blockCells = new ArrayList<>();
            for (int cellIndex = 0; cellIndex < sideLength; cellIndex++) {
                final int rowIndex = blockIndex / order * order + cellIndex / order;
                final int columnIndex = blockIndex % order * order + cellIndex % order;
                blockCells.add(cells.get(sideLength * rowIndex + columnIndex));
            }
            blocks.add(new Region<>(blockCells, targetSet));
        }

        rowCover = new Cover<>(rows);
        columnCover = new Cover<>(columns);
        blockCover = new Cover<>(blocks);

        covers = Arrays.asList(rowCover, columnCover, blockCover);

        peerMap = new LinkedHashMap<>();
        for (final Cell<T> cell : cells) {
            final Set<Cell<T>> peers = new HashSet<>();

            final int rowIndex = cell.getRowIndex();
            final int columnIndex = cell.getColumnIndex();

            for (int k = 0; k < sideLength; k++) {
                if (k != columnIndex)
                    peers.add(rows.get(rowIndex).get(k));
            }
            for (int k = 0; k < sideLength; k++) {
                if (k != rowIndex)
                    peers.add(columns.get(columnIndex).get(k));
            }
            final int blockIndex = columnIndex / order + rowIndex / order * order;
            final int cellIndex = columnIndex % order + rowIndex % order * order;
            for (int k = 0; k < sideLength; k++) {
                if (k != cellIndex)
                    peers.add(blocks.get(blockIndex).get(k));
            }
            peerMap.put(cell, peers);
        }
    }

    public Grid(final Grid<T> grid, final Cell<T> cell, final T t) {
        this(grid);
        getCells().get(cell.getRowIndex() * sideLength + cell.getColumnIndex()).setValue(t);
    }

    private int getOrder(final Set<T> targetSet) {
        final int order = (int) Math.sqrt(targetSet.size());
        if (order * order != targetSet.size())
            throw new IllegalArgumentException("Cardinality of target set is not a perfect square!");
        return order;
    }

    void updatePeers(final Cell<T> cell) {
        peerMap.get(cell).forEach(peerCell -> peerCell.removeCandidateValue(cell.getValue()));
    }

    public List<Cell<T>> getCells() {
        return cells;
    }

    @Override
    protected Stream<Cover<T>> getChildren() {
        return covers.stream();
    }

    public List<Cover<T>> getCovers() {
        return covers;
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

    public static Grid<Integer> valueOf(final String string) {
        if (!string.matches("\\d{81}"))
            throw new IllegalArgumentException();
        final Grid<Integer> grid = new Grid<>(IntStream.rangeClosed(1, 9).boxed().collect(toSet()));
        for (int i = 0; i < 81; i++) {
            final char digit = string.charAt(i);
            if (digit != '0') {
                grid.getCells().get(i).setValue(digit - '0');
            }
        }
        return grid;
    }

    public Optional<CellFill<T>> accept(final Strategy strategy) {
        return strategy.apply(this);
    }
}
