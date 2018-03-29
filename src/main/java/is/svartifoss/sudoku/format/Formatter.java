package is.svartifoss.sudoku.format;

import is.svartifoss.sudoku.model.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

import static is.svartifoss.sudoku.format.Glyph.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Formatter {

    private final Style style;

    public Formatter(final Style style) {
        this.style = style;
    }

    public <T> String format(final Grid<T> grid) {
        return new LineFormatter<>(grid).run().stream().collect(joining(System.lineSeparator()));
    }

    private class LineFormatter<T> {

        private final Grid<T> grid;

        private final int order;

        private final int width;

        private final int lastLineIndex;

        private final int contentWidth;

        private LineFormatter(final Grid<T> grid) {
            this.grid = grid;
            order = grid.getOrder();
            lastLineIndex = 2 * order * order;
            width = lastLineIndex + 1;
            contentWidth = getMaximumLength(grid.getTargetSet());
        }

        private List<String> run() {
            final String horizontalHeavyGlyph = Formatter.this.style.getGlyph(HORIZONTAL_HEAVY);
            final String horizontalLightGlyph = Formatter.this.style.getGlyph(HORIZONTAL_LIGHT);
            final String heavyHorizontal = makeContentWidth(horizontalHeavyGlyph, horizontalHeavyGlyph);
            final String lightHorizontal = makeContentWidth(horizontalLightGlyph, horizontalLightGlyph);

            final String firstLine = buildLine(RIGHT_HEAVY_DOWN_HEAVY, LEFT_HEAVY_DOWN_HEAVY, HORIZONTAL_HEAVY_DOWN_HEAVY, HORIZONTAL_HEAVY_DOWN_LIGHT, index -> heavyHorizontal);
            final String lastLine = buildLine(RIGHT_HEAVY_UP_HEAVY, LEFT_HEAVY_UP_HEAVY, HORIZONTAL_HEAVY_UP_HEAVY, HORIZONTAL_HEAVY_UP_LIGHT, index -> heavyHorizontal);
            final String heavySeparatorLine = buildLine(RIGHT_HEAVY_VERTICAL_HEAVY, LEFT_HEAVY_VERTICAL_HEAVY, HORIZONTAL_HEAVY_VERTICAL_HEAVY, HORIZONTAL_HEAVY_VERTICAL_LIGHT, index -> heavyHorizontal);
            final String lightSeparatorLine = buildLine(RIGHT_LIGHT_VERTICAL_HEAVY, LEFT_LIGHT_VERTICAL_HEAVY, HORIZONTAL_LIGHT_VERTICAL_HEAVY, HORIZONTAL_LIGHT_VERTICAL_LIGHT, index -> lightHorizontal);

            final List<String> valueLines = IntStream.range(0, grid.getSideLength())
                    .mapToObj(y ->
                            buildLine(VERTICAL_HEAVY, VERTICAL_HEAVY, VERTICAL_HEAVY, VERTICAL_LIGHT, x ->
                                    makeContentWidth(" ", Optional.ofNullable(grid.getCells().get(grid.getSideLength() * y + x).getValue()).map(Object::toString).orElse(" "))
                            )
                    ).collect(toList());

            final List<String> result = new ArrayList<>();
            for (int y = 0; y < width; y++) {
                if (y == 0) {
                    result.add(firstLine);
                } else if (y == lastLineIndex) {
                    result.add(lastLine);
                } else if (y % (2 * order) == 0) {
                    result.add(heavySeparatorLine);
                } else if (y % 2 == 0) {
                    result.add(lightSeparatorLine);
                } else {
                    result.add(valueLines.get((y - 1) / 2));
                }
            }

            return result;
        }

        private String buildLine(final Glyph firstGlyph, final Glyph lastGlyph, final Glyph heavySep, final Glyph lightSep, final Function<Integer, String> contentProvider) {
            final StringBuilder rowBuilder = new StringBuilder();
            for (int x = 0; x < width; x++) {
                if (x == 0) {
                    rowBuilder.append(style.getGlyph(firstGlyph));
                } else if (x == lastLineIndex) {
                    rowBuilder.append(style.getGlyph(lastGlyph));
                } else if (x % (2 * order) == 0) {
                    rowBuilder.append(style.getGlyph(heavySep));
                } else if (x % 2 == 0) {
                    rowBuilder.append(style.getGlyph(lightSep));
                } else {
                    rowBuilder.append(contentProvider.apply((x - 1) / 2));
                }
            }
            return rowBuilder.toString();
        }

        private String makeContentWidth(final String fill, final String content) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i <= contentWidth-content.length(); i++) {
                stringBuilder.append(fill);
            }
            stringBuilder.append(content);
            stringBuilder.append(fill);
            return stringBuilder.toString();
        }
    }

    private static <T> int getMaximumLength(final Iterable<T> iterable) {
        int result = 1;
        for (final T t : iterable) {
            result = Math.max(result, t.toString().length());
        }
        return result;
    }
}