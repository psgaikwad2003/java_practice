package DesignPatterns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Demonstrates the Flyweight Design Pattern (Structural Pattern).
 *
 * Intent: Use sharing to support large numbers of fine-grained objects efficiently.
 *
 * Core Principles:
 * 1. Intrinsic State: Invariant, context-independent data stored inside the flyweight.
 *    Shared across many contexts and made strictly immutable.
 * 2. Extrinsic State: Context-dependent data that varies per instance. Stored or computed
 *    by the client and passed to the flyweight's methods upon invocation.
 * 3. Flyweight Factory: Maintains a cache/pool of existing flyweights, returning existing
 *    instances or creating new ones on demand.
 *
 * Real-World Demonstrations:
 * 1. Massive Open-World Forest Rendering Engine:
 *    - Intrinsic Flyweight: {@link TreeType} (species name, foliage color, 3D texture mesh payload)
 *    - Flyweight Factory: {@link TreeFactory} (thread-safe cache of shared tree types)
 *    - Extrinsic Context: {@link Tree} (x, y coordinates, scale factor, age in years)
 *    - Memory Benchmark: Naive unshared objects vs. Flyweight shared instances across 100,000 trees.
 * 2. Rich Text Editor Glyph & Typography Engine:
 *    - Intrinsic Flyweight: {@link CharacterFormat} (font family, font size, bold, italic, hex color)
 *    - Extrinsic Context: {@link TextGlyph} (unicode character, line number, column offset)
 */
public class FlyweightPatternDemo {

    // =========================================================================
    // Example 1: Massive Forest World Rendering Engine
    // =========================================================================

    /**
     * Intrinsic Flyweight: Shared Tree Type.
     * Stores invariant heavy properties such as species name, foliage color,
     * and a simulated high-resolution 3D texture/mesh buffer.
     */
    public static class TreeType {
        private final String name;
        private final String color;
        private final byte[] textureMeshBuffer; // Simulated heavy asset (e.g. 10 KB per type)

        public TreeType(String name, String color) {
            this.name = name;
            this.color = color;
            // Simulate loading a heavy 10 KB texture sprite into memory
            this.textureMeshBuffer = new byte[10 * 1024];
        }

        public String getName() { return name; }
        public String getColor() { return color; }
        public int getMeshSizeBytes() { return textureMeshBuffer.length; }

        /**
         * Renders the tree by receiving its extrinsic coordinates and physical scale.
         */
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

    /**
     * Flyweight Factory: Manages creation and caching of shared {@link TreeType} objects.
     */
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

    /**
     * Extrinsic Context: A specific tree located in the world.
     * Holds only fine-grained unique coordinates and state, with a lightweight
     * reference to the shared {@link TreeType} flyweight.
     */
    public static class Tree {
        private final int x;
        private final int y;
        private final double heightMeters;
        private final int ageYears;
        private final TreeType type; // Lightweight pointer to shared flyweight

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

    /**
     * Client: Manages the entire forest consisting of thousands of trees.
     */
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

    // =========================================================================
    // Example 2: Rich Text Editor Character & Typography Formatting Engine
    // =========================================================================

    /**
     * Intrinsic Flyweight: Typographic style and format.
     */
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

    /**
     * Flyweight Factory for character typography formats.
     */
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

    /**
     * Extrinsic Context: A single character glyph within a document.
     */
    public static class TextGlyph {
        private final char character;
        private final int line;
        private final int column;
        private final CharacterFormat format; // Flyweight reference

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

    /**
     * Formatted document composed of glyphs.
     */
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

    // =========================================================================
    // Demonstration Main Method
    // =========================================================================

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("       FLYWEIGHT DESIGN PATTERN DEMONSTRATION (STRUCTURAL)");
        System.out.println("======================================================================");

        // ---------------------------------------------------------------------
        // Demo 1: Massive Forest World Rendering & Memory Optimization
        // ---------------------------------------------------------------------
        System.out.println("\n--- [Demo 1] Massive Forest Rendering (100,000 Trees) ---");

        Forest forest = new Forest();
        String[] species = {"Oak", "Pine", "Birch", "Maple", "Redwood"};
        String[] colors = {"Emerald Green", "Deep Forest Green", "Autumn Gold", "Crimson Red", "Dark Pine"};

        Random rng = new Random(42); // Deterministic seed for reproducible testing
        int totalTrees = 100_000;

        System.out.printf("Planting %d trees across a 10,000 x 10,000 game terrain...%n", totalTrees);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < totalTrees; i++) {
            int x = rng.nextInt(10000);
            int y = rng.nextInt(10000);
            double height = 2.0 + rng.nextDouble() * 25.0; // 2m to 27m tall
            int age = 1 + rng.nextInt(150); // 1 to 150 years
            int idx = rng.nextInt(species.length);

            forest.plantTree(x, y, height, age, species[idx], colors[idx]);
        }
        long elapsedTime = System.currentTimeMillis() - startTime;

        System.out.printf("Successfully planted %d trees in %d ms.%n", forest.getTreeCount(), elapsedTime);
        System.out.printf("Unique TreeType flyweight instances in memory: %d%n", TreeFactory.getCreatedTypesCount());
        System.out.printf("Flyweight Factory Stats -> Cache Hits: %d | Cache Misses: %d%n",
                TreeFactory.getCacheHits(), TreeFactory.getCacheMisses());

        // Sample render output of first 3 trees
        System.out.println("\nSampling visual output for first 3 trees:");
        for (Tree sample : forest.getSampleTrees(3)) {
            sample.draw();
        }

        // Memory Analysis Calculation:
        // Naive: 100,000 trees * (10 KB texture mesh + ~48 bytes object overhead) ~= ~1,000 MB (1 GB)
        // Flyweight: 5 TreeTypes * 10 KB = 50 KB + 100,000 * ~24 bytes extrinsic state ~= 2.45 MB
        long naiveBytes = (long) totalTrees * (10 * 1024 + 48);
        long flyweightBytes = (long) TreeFactory.getCreatedTypesCount() * (10 * 1024 + 48) + (long) totalTrees * 32;
        double naiveMb = naiveBytes / (1024.0 * 1024.0);
        double flyweightMb = flyweightBytes / (1024.0 * 1024.0);
        double savingsPercent = ((naiveMb - flyweightMb) / naiveMb) * 100.0;

        System.out.println("\n[Theoretical Memory Footprint Comparison]");
        System.out.printf("  - Naive Unshared Allocation:  ~%.2f MB%n", naiveMb);
        System.out.printf("  - Flyweight Shared Footprint: ~%.2f MB%n", flyweightMb);
        System.out.printf("  - Memory Consumption Savings:  %.2f%%%n", savingsPercent);

        // ---------------------------------------------------------------------
        // Demo 2: Rich Text Editor Glyph Formatting
        // ---------------------------------------------------------------------
        System.out.println("\n--- [Demo 2] Rich Text Document Glyph Rendering ---");

        TextDocument doc = new TextDocument();
        String text = "Design Patterns in Java 21";

        // Style Title characters: "Design " (Heading 18pt Bold)
        for (int i = 0; i < "Design ".length(); i++) {
            doc.append(text.charAt(i), 1, i + 1, "JetBrains Mono", 18, true, false, "#FFB800");
        }
        // Style "Patterns " (Heading 18pt Bold Italic)
        int offset = "Design ".length();
        for (int i = 0; i < "Patterns ".length(); i++) {
            doc.append(text.charAt(offset + i), 1, offset + i + 1, "JetBrains Mono", 18, true, true, "#00E676");
        }
        // Style "in Java 21" (Heading 18pt Regular)
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
