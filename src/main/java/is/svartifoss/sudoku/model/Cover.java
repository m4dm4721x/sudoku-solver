package is.svartifoss.sudoku.model;

import java.util.List;
import java.util.stream.Stream;

public class Cover<T> extends CompositeSolvable<Region<T>> {

    private final List<Region<T>> regions;

    public Cover(final List<Region<T>> regions) {
        this.regions = regions;
    }

    @Override
    protected Stream<Region<T>> getChildren() {
        return regions.stream();
    }

    @Override
    public boolean isFeasible() {
        return regions.stream().allMatch(Region::isFeasible);
    }

    @Override
    public boolean isSolved() {
        return regions.stream().allMatch(Region::isSolved);
    }

    public List<Region<T>> getRegions() {
        return regions;
    }
}
