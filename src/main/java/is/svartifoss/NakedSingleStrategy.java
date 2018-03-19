package is.svartifoss;

import java.util.List;
import java.util.Set;

public class NakedSingleStrategy implements Strategy {
    @Override
    public boolean apply(final Grid grid) {
        for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                final List<Integer> cellCandidates = grid.getCell(rowIndex, columnIndex).getCandidates();
                if (cellCandidates.size() == 1) {
                    grid.getCell(rowIndex, columnIndex).setValue(cellCandidates.iterator().next());
                    return true;
                }
            }
        }
        return false;
    }
}
