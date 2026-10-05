package DesignPatterns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class FlyweightPatternDemo {

    public static class TreeType {
        private final String name;
        private final String color;
        private final byte[] textureMeshBuffer;

        public TreeType(String name, String color) {
            this.name = name;
            this.color = color;

            this.textureMeshBuffer = new byte[10 * 1024];
        }

        public String getName() { return name; }
        public String getColor() { return color; }
        public int getMeshSizeBytes() { return textureMeshBuffer.length; }

        public void render(int x, int y, double heightMeters, int ageYears) {
            System.out.printf("    Rendering [%s - %s] at coordinates (%d, %d) | Height: %.1fm | Age: %dyrs%n",
                    name, color, x, y, heightMeters, ageYears);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TreeType treeType = (TreeType) o;
            return Objects.equals(name, treeType.name) && Objects.equals(color, treeType.color);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, color);
        }
    }

    public static class TreeFactory {
        private static final Map<String, TreeType> treeTypes = new HashMap<>();
        private static int cacheHits = 0;
        private static int cacheMisses = 0;

        public static synchronized TreeType getTreeType(String name, String color) {
            String key = name.toLowerCase() + "_" + color.toLowerCase();
            TreeType type = treeTypes.get(key);
            if (type == null) {
                cacheMisses++;
                type = new TreeType(name, color);
                treeTypes.put(key, type);
            } else {
                cacheHits++;
            }
            return type;
        }

        public static int getCreatedTypesCount() {
            return treeTypes.size();
        }

        public static int getCacheHits() {
            return cacheHits;
        }

        public static int getCacheMisses() {
            return cacheMisses;
        }
    }

    public static class Tree {
        private final int x;
        private final int y;
        private final double heightMeters;
        private final int ageYears;
        private final TreeType type;

        public Tree(int x, int y, double heightMeters, int ageYears, TreeType type) {
            this.x = x;
            this.y = y;
            this.heightMeters = heightMeters;
            this.ageYears = ageYears;
            this.type = type;
        }

        public void draw() {
            type.render(x, y, heightMeters, ageYears);
        }
    }

    public static class Forest {
        private final List<Tree> trees = new ArrayList<>();

        public void plantTree(int x, int y, double height, int age, String name, String color) {
            TreeType type = TreeFactory.getTreeType(name, color);
            Tree tree = new Tree(x, y, height, age, type);
            trees.add(tree);
        }

        public int getTreeCount() {
            return trees.size();
        }

        public List<Tree> getSampleTrees(int count) {
            return trees.subList(0, Math.min(count, trees.size()));
        }
    }

    public static class CharacterFormat {
        private final String fontFamily;
        private final int fontSize;
        private final boolean bold;
        private final boolean italic;
        private final String hexColor;

        public CharacterFormat(String fontFamily, int fontSize, boolean bold, boolean italic, String hexColor) {
            this.fontFamily = fontFamily;
            this.fontSize = fontSize;
            this.bold = bold;
            this.italic = italic;
            this.hexColor = hexColor;
        }

        public void applyFormat(char ch, int line, int column) {
            String style = (bold ? "B" : "") + (italic ? "I" : "");
            if (style.isEmpty()) style = "Regular";
            System.out.printf("  Char '%c' at [L%d:C%d] -> %s %dpt (%s) [%s]%n",
                    ch, line, column, fontFamily, fontSize, style, hexColor);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CharacterFormat that = (CharacterFormat) o;
            return fontSize == that.fontSize &&
                    bold == that.bold &&
                    italic == that.italic &&
                    Objects.equals(fontFamily, that.fontFamily) &&
                    Objects.equals(hexColor, that.hexColor);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fontFamily, fontSize, bold, italic, hexColor);
        }
    }

    public static class FormatFactory {
        private static final Map<String, CharacterFormat> formatPool = new HashMap<>();

        public static synchronized CharacterFormat getFormat(String family, int size, boolean bold, boolean italic, String color) {
            String key = String.format("%s_%d_%b_%b_%s", family, size, bold, italic, color);
            return formatPool.computeIfAbsent(key, k -> new CharacterFormat(family, size, bold, italic, color));
        }

        public static int getPoolSize() {
            return formatPool.size();
        }
    }

    public static class TextGlyph {
        private final char character;
        private final int line;
        private final int column;
        private final CharacterFormat format;

        public TextGlyph(char character, int line, int column, CharacterFormat format) {
            this.character = character;
            this.line = line;
            this.column = column;
            this.format = format;
        }

        public void render() {
            format.applyFormat(character, line, column);
        }
    }

    public static class TextDocument {
        private final List<TextGlyph> glyphs = new ArrayList<>();

        public void append(char ch, int line, int col, String font, int size, boolean bold, boolean italic, String color) {
            CharacterFormat format = FormatFactory.getFormat(font, size, bold, italic, color);
            glyphs.add(new TextGlyph(ch, line, col, format));
        }

        public int getLength() {
            return glyphs.size();
        }

        public List<TextGlyph> getGlyphs() {
            return Collections.unmodifiableList(glyphs);
        }
    }

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("       FLYWEIGHT DESIGN PATTERN DEMONSTRATION (STRUCTURAL)");
        System.out.println("======================================================================");

        System.out.println("\n--- [Demo 1] Massive Forest Rendering (100,000 Trees) ---");

        Forest forest = new Forest();
        String[] species = {"Oak", "Pine", "Birch", "Maple", "Redwood"};
        String[] colors = {"Emerald Green", "Deep Forest Green", "Autumn Gold", "Crimson Red", "Dark Pine"};

        Random rng = new Random(42);
        int totalTrees = 100_000;

        System.out.printf("Planting %d trees across a 10,000 x 10,000 game terrain...%n", totalTrees);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < totalTrees; i++) {
            int x = rng.nextInt(10000);
            int y = rng.nextInt(10000);
            double height = 2.0 + rng.nextDouble() * 25.0;
            int age = 1 + rng.nextInt(150);
            int idx = rng.nextInt(species.length);

            forest.plantTree(x, y, height, age, species[idx], colors[idx]);
        }
        long elapsedTime = System.currentTimeMillis() - startTime;

        System.out.printf("Successfully planted %d trees in %d ms.%n", forest.getTreeCount(), elapsedTime);
        System.out.printf("Unique TreeType flyweight instances in memory: %d%n", TreeFactory.getCreatedTypesCount());
        System.out.printf("Flyweight Factory Stats -> Cache Hits: %d | Cache Misses: %d%n",
                TreeFactory.getCacheHits(), TreeFactory.getCacheMisses());

        System.out.println("\nSampling visual output for first 3 trees:");
        for (Tree sample : forest.getSampleTrees(3)) {
            sample.draw();
        }

        long naiveBytes = (long) totalTrees * (10 * 1024 + 48);
        long flyweightBytes = (long) TreeFactory.getCreatedTypesCount() * (10 * 1024 + 48) + (long) totalTrees * 32;
        double naiveMb = naiveBytes / (1024.0 * 1024.0);
        double flyweightMb = flyweightBytes / (1024.0 * 1024.0);
        double savingsPercent = ((naiveMb - flyweightMb) / naiveMb) * 100.0;

        System.out.println("\n[Theoretical Memory Footprint Comparison]");
        System.out.printf("  - Naive Unshared Allocation:  ~%.2f MB%n", naiveMb);
        System.out.printf("  - Flyweight Shared Footprint: ~%.2f MB%n", flyweightMb);
        System.out.printf("  - Memory Consumption Savings:  %.2f%%%n", savingsPercent);

        System.out.println("\n--- [Demo 2] Rich Text Document Glyph Rendering ---");

        TextDocument doc = new TextDocument();
        String text = "Design Patterns in Java 21";

        for (int i = 0; i < "Design ".length(); i++) {
            doc.append(text.charAt(i), 1, i + 1, "JetBrains Mono", 18, true, false, "#FFB800");
        }

        int offset = "Design ".length();
        for (int i = 0; i < "Patterns ".length(); i++) {
            doc.append(text.charAt(offset + i), 1, offset + i + 1, "JetBrains Mono", 18, true, true, "#00E676");
        }

        offset += "Patterns ".length();
        for (int i = offset; i < text.length(); i++) {
            doc.append(text.charAt(i), 1, i + 1, "JetBrains Mono", 18, false, false, "#FFFFFF");
        }

        System.out.printf("Document contains %d rendered characters with only %d shared format flyweights.%n",
                doc.getLength(), FormatFactory.getPoolSize());

        System.out.println("\nRendering formatted glyph stream:");
        for (TextGlyph glyph : doc.getGlyphs()) {
            glyph.render();
        }

        System.out.println("\n======================================================================");
        System.out.println("  Flyweight Pattern Advantages:");
        System.out.println("  - Drastically slashes heap memory usage when handling millions of objects.");
        System.out.println("  - Centralizes shared invariant state into immutable, reusable instances.");
        System.out.println("  - Enhances CPU cache locality by minimizing total working set footprint.");
        System.out.println("======================================================================");
    }
}
