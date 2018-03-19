package is.svartifoss;

public class HiddenSingleStrategy implements Strategy {
    @Override
    public boolean apply(final Grid grid) {
        return applyToRegion(grid.getRows()) || applyToRegion(grid.getColumns()) || applyToRegion(grid.getBlocks());
    }

    private boolean applyToRegion(final Grid.Cell[][] regions) {
        for (final Grid.Cell[] region : regions) {
            for (int value = 0; value < 9; value++) {
                int occurrences = 0;
                Grid.Cell cellWithHiddenSingleCandidate = null;
                for (final Grid.Cell cell : region) {
                    if (cell.getCandidates().contains(value)) {
                        occurrences += 1;
                        cellWithHiddenSingleCandidate = cell;
                    }
                }
                if (occurrences == 1) {
                    cellWithHiddenSingleCandidate.setValue(value);
                    return true;
                }
            }
        }
        return false;
    }
}
