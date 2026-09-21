package DesignPatterns;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates the Memento Design Pattern (Behavioral Pattern).
 * 
 * Intent: Without violating encapsulation, capture and externalize an object's
 * internal state so that the object can be restored to this state later.
 * 
 * Key Participants:
 * 1. Originator: Creates a memento containing a snapshot of its internal state
 *    and uses the memento to restore its state.
 * 2. Memento: Stores the internal state of the Originator. Protects against access
 *    by objects other than the Originator (private inner implementation).
 * 3. Caretaker: Responsible for memento safekeeping (history stacks, checkpoint stores);
 *    never examines or modifies the memento contents.
 * 
 * Included Examples:
 * 1. IDE Code/Text Editor: Full multi-level Undo & Redo history, tracking text buffers,
 *    cursor line/column coordinates, and styling metadata.
 * 2. RPG Game State / Checkpoint Manager: Savepoint checkpoints, inventory state,
 *    boss fight progression, and rollback recovery.
 */
public class MementoPatternDemo {

    // =========================================================================
    // Example 1: Code & Text Editor with Multi-Level Undo / Redo Stacks
    // =========================================================================

    /**
     * Memento Interface: Narrow interface presented to the Caretaker (metadata only).
     */
    public interface DocumentMemento {
        String getSummary();
        LocalDateTime getTimestamp();
    }

    /**
     * Originator: The Text Editor whose state changes as users type and format text.
     */
    public static class TextEditorOriginator {
        private StringBuilder content;
        private int cursorLine;
        private int cursorColumn;
        private String currentFont;
        private int fontSize;

        public TextEditorOriginator() {
            this.content = new StringBuilder();
            this.cursorLine = 1;
            this.cursorColumn = 1;
            this.currentFont = "JetBrains Mono";
            this.fontSize = 14;
        }

        public void type(String text) {
            content.append(text);
            cursorColumn += text.length();
        }

        public void addNewLine() {
            content.append("\n");
            cursorLine++;
            cursorColumn = 1;
        }

        public void setFormatting(String font, int size) {
            this.currentFont = font;
            this.fontSize = size;
        }

        public void clear() {
            content.setLength(0);
            cursorLine = 1;
            cursorColumn = 1;
        }

        public String getContent() {
            return content.toString();
        }

        public void printCurrentState() {
            System.out.printf("  [Editor State] Font: %s (%dpt) | Cursor: [Ln %d, Col %d]%n",
                    currentFont, fontSize, cursorLine, cursorColumn);
            System.out.println("  ---------------------------------------------");
            if (content.length() == 0) {
                System.out.println("  (empty document)");
            } else {
                for (String line : content.toString().split("\n")) {
                    System.out.printf("  | %s%n", line);
                }
            }
            System.out.println("  ---------------------------------------------");
        }

        /**
         * Creates a snapshot Memento of current internal state.
         */
        public DocumentMemento saveSnapshot(String description) {
            return new EditorMementoImpl(content.toString(), cursorLine, cursorColumn, currentFont, fontSize, description);
        }

        /**
         * Restores internal state from a given Memento.
         */
        public void restoreSnapshot(DocumentMemento memento) {
            if (!(memento instanceof EditorMementoImpl impl)) {
                throw new IllegalArgumentException("Unknown memento implementation: " + memento);
            }
            this.content = new StringBuilder(impl.savedContent);
            this.cursorLine = impl.savedCursorLine;
            this.cursorColumn = impl.savedCursorColumn;
            this.currentFont = impl.savedFont;
            this.fontSize = impl.savedFontSize;
            System.out.printf("  -> State restored to snapshot: \"%s\" (saved at %s)%n",
                    impl.description, impl.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
        }

        /**
         * Concrete Memento: Private inner class ensuring strict encapsulation.
         * The outer Originator has full access to private fields, while the outside world cannot tamper with them.
         */
        private static class EditorMementoImpl implements DocumentMemento {
            private final String savedContent;
            private final int savedCursorLine;
            private final int savedCursorColumn;
            private final String savedFont;
            private final int savedFontSize;
            private final String description;
            private final LocalDateTime timestamp;

            private EditorMementoImpl(String content, int cursorLine, int cursorColumn,
                                      String font, int fontSize, String description) {
                this.savedContent = content;
                this.savedCursorLine = cursorLine;
                this.savedCursorColumn = cursorColumn;
                this.savedFont = font;
                this.savedFontSize = fontSize;
                this.description = description;
                this.timestamp = LocalDateTime.now();
            }

            @Override
            public String getSummary() {
                return String.format("%s (Length: %d chars)", description, savedContent.length());
            }

            @Override
            public LocalDateTime getTimestamp() {
                return timestamp;
            }
        }
    }

    /**
     * Caretaker: Manages Undo and Redo stacks for the text editor.
     */
    public static class EditorHistoryManager {
        private final TextEditorOriginator editor;
        private final Deque<DocumentMemento> undoStack = new ArrayDeque<>();
        private final Deque<DocumentMemento> redoStack = new ArrayDeque<>();
        private final int maxHistoryDepth;

        public EditorHistoryManager(TextEditorOriginator editor, int maxHistoryDepth) {
            this.editor = editor;
            this.maxHistoryDepth = maxHistoryDepth;
        }

        public void recordCheckpoint(String description) {
            DocumentMemento snapshot = editor.saveSnapshot(description);
            if (undoStack.size() >= maxHistoryDepth) {
                undoStack.removeLast(); // drop oldest snapshot
            }
            undoStack.push(snapshot);
            redoStack.clear(); // Any new user edit invalidates redo history
            System.out.printf("[History] Saved checkpoint: \"%s\" (Undo stack depth: %d)%n",
                    description, undoStack.size());
        }

        public boolean undo() {
            if (undoStack.isEmpty()) {
                System.out.println("[History] Nothing to undo! Stack is empty.");
                return false;
            }

            // Save current state to redo stack before applying previous state
            DocumentMemento currentSnapshot = editor.saveSnapshot("Pre-Undo state");
            redoStack.push(currentSnapshot);

            DocumentMemento previousSnapshot = undoStack.pop();
            System.out.printf("[History] Performing UNDO -> rolling back: \"%s\"%n", previousSnapshot.getSummary());
            editor.restoreSnapshot(previousSnapshot);
            return true;
        }

        public boolean redo() {
            if (redoStack.isEmpty()) {
                System.out.println("[History] Nothing to redo! Stack is empty.");
                return false;
            }

            DocumentMemento currentSnapshot = editor.saveSnapshot("Pre-Redo state");
            undoStack.push(currentSnapshot);

            DocumentMemento redoSnapshot = redoStack.pop();
            System.out.printf("[History] Performing REDO -> reapplying: \"%s\"%n", redoSnapshot.getSummary());
            editor.restoreSnapshot(redoSnapshot);
            return true;
        }
    }

    // =========================================================================
    // Example 2: RPG Game State Savepoint & Rollback Recovery
    // =========================================================================

    /**
     * Originator: Player character game state.
     */
    public static class GameCharacterOriginator {
        private String name;
        private int level;
        private int health;
        private int mana;
        private final List<String> inventory = new ArrayList<>();
        private String currentQuest;

        public GameCharacterOriginator(String name) {
            this.name = name;
            this.level = 1;
            this.health = 100;
            this.mana = 50;
            this.currentQuest = "Tutorial: Awakening";
            this.inventory.add("Wooden Sword");
            this.inventory.add("Health Potion (x3)");
        }

        public void takeDamage(int amount) {
            this.health = Math.max(0, this.health - amount);
            System.out.printf("  [%s] Took %d damage! Health: %d/100%n", name, amount, health);
        }

        public void castSpell(String spell, int manaCost) {
            if (this.mana >= manaCost) {
                this.mana -= manaCost;
                System.out.printf("  [%s] Cast %s (-%d MP). Mana remaining: %d%n", name, spell, manaCost, mana);
            } else {
                System.out.printf("  [%s] Not enough mana to cast %s!%n", name, spell);
            }
        }

        public void acquireLoot(String item) {
            inventory.add(item);
            System.out.printf("  [%s] Acquired item: [%s]!%n", name, item);
        }

        public void advanceQuest(String newQuest, int levelBonus) {
            this.currentQuest = newQuest;
            this.level += levelBonus;
            this.health = 100;
            this.mana += 25;
            System.out.printf("  [%s] Quest Updated: \"%s\"! Level Up -> Level %d!%n", name, newQuest, level);
        }

        public void displayStatus() {
            System.out.printf("  Hero: %s | Lvl %d | HP: %d | MP: %d | Quest: %s%n",
                    name, level, health, mana, currentQuest);
            System.out.printf("  Inventory: %s%n", inventory);
        }

        public GameSavepointMemento createSavepoint(String savepointName) {
            return new GameSavepointMemento(savepointName, level, health, mana, currentQuest, new ArrayList<>(inventory));
        }

        public void loadSavepoint(GameSavepointMemento savepoint) {
            this.level = savepoint.level;
            this.health = savepoint.health;
            this.mana = savepoint.mana;
            this.currentQuest = savepoint.currentQuest;
            this.inventory.clear();
            this.inventory.addAll(savepoint.inventory);
            System.out.printf("  [Load Game] Restored savepoint: \"%s\" (Timestamp: %s)%n",
                    savepoint.savepointName, savepoint.savedAt.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }

        /**
         * Memento Object for Game State.
         */
        public static class GameSavepointMemento {
            private final String savepointName;
            private final int level;
            private final int health;
            private final int mana;
            private final String currentQuest;
            private final List<String> inventory;
            private final LocalDateTime savedAt;

            private GameSavepointMemento(String savepointName, int level, int health, int mana,
                                         String currentQuest, List<String> inventory) {
                this.savepointName = savepointName;
                this.level = level;
                this.health = health;
                this.mana = mana;
                this.currentQuest = currentQuest;
                this.inventory = Collections.unmodifiableList(inventory);
                this.savedAt = LocalDateTime.now();
            }

            public String getSavepointName() {
                return savepointName;
            }
        }
    }

    /**
     * Caretaker: Game Save Slot & Checkpoint Manager.
     */
    public static class GameCheckpointManager {
        private final Map<String, GameCharacterOriginator.GameSavepointMemento> saveSlots = new HashMap<>();

        public void saveCheckpoint(String slotKey, GameCharacterOriginator.GameSavepointMemento memento) {
            saveSlots.put(slotKey, memento);
            System.out.printf("[Save Manager] Saved checkpoint to slot [%s]: \"%s\"%n", slotKey, memento.getSavepointName());
        }

        public GameCharacterOriginator.GameSavepointMemento getCheckpoint(String slotKey) {
            return saveSlots.get(slotKey);
        }
    }

    // =========================================================================
    // Demonstration & Test Runner
    // =========================================================================

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("       MEMENTO PATTERN DEMO - STATE SNAPSHOTS & RESTORATION      ");
        System.out.println("=================================================================");

        // --- Demo 1: Code/Text Editor Undo/Redo Engine ---
        System.out.println("\n--- SCENARIO 1: IDE TEXT EDITOR MULTI-LEVEL UNDO/REDO ---");
        TextEditorOriginator editor = new TextEditorOriginator();
        EditorHistoryManager history = new EditorHistoryManager(editor, 10);

        // State 0: Initial empty
        history.recordCheckpoint("Blank Document");

        // Action 1: Type class definition
        editor.type("public class PaymentGateway {");
        editor.addNewLine();
        editor.type("    public void processPayment() {}");
        history.recordCheckpoint("Initial method stub");

        editor.printCurrentState();

        // Action 2: Add validation logic & change styling
        editor.addNewLine();
        editor.type("    // Validating card security digits");
        editor.setFormatting("Fira Code", 16);
        history.recordCheckpoint("Added comments and changed font");

        editor.printCurrentState();

        // Action 3: Accidental deletion
        System.out.println("\n[Action] Accidental deletion of code by user:");
        editor.clear();
        editor.type("// Oops, accidental clear!");
        editor.printCurrentState();

        // Perform Undo
        System.out.println("\n[Action] User presses Ctrl+Z (Undo):");
        history.undo();
        editor.printCurrentState();

        // Perform another Undo
        System.out.println("\n[Action] User presses Ctrl+Z again (Undo):");
        history.undo();
        editor.printCurrentState();

        // Perform Redo
        System.out.println("\n[Action] User presses Ctrl+Y (Redo):");
        history.redo();
        editor.printCurrentState();

        // --- Demo 2: RPG Game Checkpoints & Boss Battle Rollback ---
        System.out.println("\n--- SCENARIO 2: RPG CHECKPOINTS & ROLLBACK ON DEFEAT ---");
        GameCharacterOriginator hero = new GameCharacterOriginator("Geralt");
        GameCheckpointManager checkpointManager = new GameCheckpointManager();

        System.out.println("\n[Initial Hero State]");
        hero.displayStatus();

        // Hero prepares before entering dungeon
        hero.advanceQuest("Explore Cursed Ruins", 2);
        hero.acquireLoot("Silver Relic Sword");
        hero.acquireLoot("Ancient Shield");

        System.out.println("\n[Action] Saving checkpoint outside the dungeon gate:");
        checkpointManager.saveCheckpoint("Dungeon_Entrance", hero.createSavepoint("Checkpoint: Before Dungeon Gate"));
        hero.displayStatus();

        // Hero engages in fierce dragon boss fight
        System.out.println("\n[Action] Entering Boss Arena against Elder Dragon:");
        hero.castSpell("Quen Shield", 20);
        hero.takeDamage(60);
        hero.castSpell("Igni Flame", 25);
        hero.takeDamage(45); // Lethal or critical hit

        System.out.println("\n[Action] Hero suffers catastrophic defeat!");
        hero.displayStatus();

        System.out.println("\n[Action] Loading checkpoint from Dungeon Entrance to retry:");
        GameCharacterOriginator.GameSavepointMemento gateSave = checkpointManager.getCheckpoint("Dungeon_Entrance");
        if (gateSave != null) {
            hero.loadSavepoint(gateSave);
        }
        hero.displayStatus();

        System.out.println("\n=================================================================");
        System.out.println("                 MEMENTO PATTERN COMPLETED                       ");
        System.out.println("=================================================================");
    }
}
