package DesignPatterns;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Demonstrates the Command Design Pattern (Behavioral Pattern) with Undo / Redo support.
 * 
 * Components:
 * 1. Command Interface: execute() and undo().
 * 2. Receiver (TextDocument): Performs the actual text mutations.
 * 3. Concrete Commands: InsertTextCommand, DeleteTextCommand, MacroCommand.
 * 4. Invoker (CommandHistoryManager): Maintains undo/redo stacks.
 */
public class CommandPatternDemo {

    // =========================================================================
    // 1. Receiver: Holds state and primitives
    // =========================================================================
    public static class TextDocument {
        private final StringBuilder content = new StringBuilder();

        public void insert(int position, String text) {
            content.insert(position, text);
        }

        public void delete(int position, int length) {
            content.delete(position, position + length);
        }

        public String getText() {
            return content.toString();
        }

        public int getLength() {
            return content.length();
        }

        @Override
        public String toString() {
            return "\"" + content.toString() + "\"";
        }
    }

    // =========================================================================
    // 2. Command Interface
    // =========================================================================
    public interface Command {
        void execute();
        void undo();
        String getDescription();
    }

    // =========================================================================
    // 3. Concrete Commands
    // =========================================================================
    public static class InsertTextCommand implements Command {
        private final TextDocument document;
        private final int position;
        private final String text;

        public InsertTextCommand(TextDocument document, int position, String text) {
            this.document = document;
            this.position = position;
            this.text = text;
        }

        @Override
        public void execute() {
            document.insert(position, text);
        }

        @Override
        public void undo() {
            document.delete(position, text.length());
        }

        @Override
        public String getDescription() {
            return "Insert \"" + text + "\" at position " + position;
        }
    }

    public static class AppendTextCommand implements Command {
        private final TextDocument document;
        private final String text;
        private int insertedPosition;

        public AppendTextCommand(TextDocument document, String text) {
            this.document = document;
            this.text = text;
        }

        @Override
        public void execute() {
            this.insertedPosition = document.getLength();
            document.insert(insertedPosition, text);
        }

        @Override
        public void undo() {
            document.delete(insertedPosition, text.length());
        }

        @Override
        public String getDescription() {
            return "Append \"" + text.replace("\n", "\\n") + "\"";
        }
    }

    public static class DeleteTextCommand implements Command {
        private final TextDocument document;
        private final int position;
        private final int length;
        private String deletedContent;

        public DeleteTextCommand(TextDocument document, int position, int length) {
            this.document = document;
            this.position = position;
            this.length = length;
        }

        @Override
        public void execute() {
            deletedContent = document.getText().substring(position, position + length);
            document.delete(position, length);
        }

        @Override
        public void undo() {
            document.insert(position, deletedContent);
        }

        @Override
        public String getDescription() {
            return "Delete " + length + " chars at position " + position;
        }
    }

    /**
     * Composite Macro Command to execute multiple commands as a single atomic batch.
     */
    public static class MacroCommand implements Command {
        private final List<Command> commands = new ArrayList<>();
        private final String name;

        public MacroCommand(String name) {
            this.name = name;
        }

        public void add(Command command) {
            commands.add(command);
        }

        @Override
        public void execute() {
            for (Command cmd : commands) {
                cmd.execute();
            }
        }

        @Override
        public void undo() {
            // Undo in reverse order of execution
            for (int i = commands.size() - 1; i >= 0; i--) {
                commands.get(i).undo();
            }
        }

        @Override
        public String getDescription() {
            return "Macro: " + name + " (" + commands.size() + " sub-commands)";
        }
    }

    // =========================================================================
    // 4. Invoker: Tracks History for Undo / Redo
    // =========================================================================
    public static class CommandHistoryManager {
        private final Deque<Command> undoStack = new ArrayDeque<>();
        private final Deque<Command> redoStack = new ArrayDeque<>();

        public void executeCommand(Command command) {
            command.execute();
            undoStack.push(command);
            redoStack.clear(); // Clear redo stack on new operation
            System.out.printf("  [Executed] %s%n", command.getDescription());
        }

        public boolean canUndo() {
            return !undoStack.isEmpty();
        }

        public boolean canRedo() {
            return !redoStack.isEmpty();
        }

        public void undo() {
            if (!canUndo()) {
                System.out.println("  [Undo] Nothing to undo.");
                return;
            }
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            System.out.printf("  [Undid]    %s%n", command.getDescription());
        }

        public void redo() {
            if (!canRedo()) {
                System.out.println("  [Redo] Nothing to redo.");
                return;
            }
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            System.out.printf("  [Redid]    %s%n", command.getDescription());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Command Design Pattern (Undo/Redo) Demo ===\n");

        TextDocument doc = new TextDocument();
        CommandHistoryManager manager = new CommandHistoryManager();

        System.out.println("Initial Document: " + doc);

        // 1. Execute sequential insert commands
        manager.executeCommand(new InsertTextCommand(doc, 0, "Hello"));
        System.out.println("  Doc: " + doc);

        manager.executeCommand(new InsertTextCommand(doc, doc.getLength(), " World"));
        System.out.println("  Doc: " + doc);

        manager.executeCommand(new InsertTextCommand(doc, doc.getLength(), "!"));
        System.out.println("  Doc: " + doc);

        // 2. Undo operations
        System.out.println("\n--- Testing Undo ---");
        manager.undo(); // removes '!'
        System.out.println("  Doc: " + doc);

        manager.undo(); // removes ' World'
        System.out.println("  Doc: " + doc);

        // 3. Redo operations
        System.out.println("\n--- Testing Redo ---");
        manager.redo(); // restores ' World'
        System.out.println("  Doc: " + doc);

        // 4. Delete operation
        System.out.println("\n--- Testing Delete Command ---");
        manager.executeCommand(new DeleteTextCommand(doc, 0, 5)); // Deletes "Hello"
        System.out.println("  Doc: " + doc);

        manager.undo(); // Restores "Hello"
        System.out.println("  Doc after undoing delete: " + doc);

        // 5. Macro Command
        System.out.println("\n--- Testing Macro / Batch Command ---");
        MacroCommand addSignatureMacro = new MacroCommand("Add Signature");
        addSignatureMacro.add(new AppendTextCommand(doc, "\nBest regards,"));
        addSignatureMacro.add(new AppendTextCommand(doc, "\nJava Design Pattern Bot"));

        manager.executeCommand(addSignatureMacro);
        System.out.println("  Doc:\n" + doc.getText());

        System.out.println("\nUndoing entire Macro in one step:");
        manager.undo();
        System.out.println("  Doc: " + doc);
    }
}
