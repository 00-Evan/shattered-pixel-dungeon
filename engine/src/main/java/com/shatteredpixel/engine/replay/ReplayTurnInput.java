package com.shatteredpixel.engine.replay;

import com.shatteredpixel.engine.command.GameCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Immutable value object representing all external GameCommands for a single turn.
 *
 * This captures:
 * - The turn index (sequential order in the replay)
 * - The list of external commands to be applied this turn
 *
 * Used by ReplayTrace to represent the complete input sequence for a replay.
 *
 * External commands are typically:
 * - Hero movement commands (player input)
 * - Hero attack commands (player input)
 * - Any other player-controlled actions
 *
 * Mob AI commands are NOT recorded here - they are deterministically
 * regenerated during replay from the same RNG seed and game state.
 */
public class ReplayTurnInput {

    private final int turnIndex;
    private final List<GameCommand> commands;

    /**
     * Create a replay turn input entry.
     *
     * @param turnIndex Sequential turn number (0-based)
     * @param commands External commands for this turn (defensive copy made)
     */
    public ReplayTurnInput(int turnIndex, List<GameCommand> commands) {
        this.turnIndex = turnIndex;
        // Defensive copy to ensure immutability
        this.commands = commands != null
            ? Collections.unmodifiableList(new ArrayList<>(commands))
            : Collections.emptyList();
    }

    /**
     * Get the turn index.
     *
     * @return Turn number (0-based)
     */
    public int getTurnIndex() {
        return turnIndex;
    }

    /**
     * Get the external commands for this turn.
     *
     * @return Immutable list of commands
     */
    public List<GameCommand> getCommands() {
        return commands;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ReplayTurnInput)) return false;
        ReplayTurnInput other = (ReplayTurnInput) obj;
        return this.turnIndex == other.turnIndex
            && this.commands.equals(other.commands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turnIndex, commands);
    }

    @Override
    public String toString() {
        return "ReplayTurnInput{" +
            "turn=" + turnIndex +
            ", commands=" + commands.size() +
            "}";
    }
}
