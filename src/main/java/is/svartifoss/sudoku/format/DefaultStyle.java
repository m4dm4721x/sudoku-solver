package is.svartifoss.sudoku.format;

public enum DefaultStyle implements Style {
    CLASSIC("┏━┯┳┓┃│┠─┼╂┨┣┿╋┫┗┷┻┛"),
    DOUBLE("╔═╤╦╗║│╟─┼╫╢╠╪╬╣╚╧╩╝"),
    ASCII("+--++|||- |||-+++-++");

    private final String glyphs;

    DefaultStyle(final String glyphs) {
        if (glyphs.length() != Glyph.values().length) {
            throw new IllegalArgumentException();
        }
        this.glyphs = glyphs;
    }

    @Override
    public String getGlyph(final Glyph glyph) {
        return String.valueOf(glyphs.charAt(glyph.ordinal()));
    }
}
