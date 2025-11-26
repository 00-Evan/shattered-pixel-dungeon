package com.shatteredpixel.engine.command;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Simple queue for GameCommand objects.
 *
 * Provides a FIFO queue for commands to be processed by the engine.
 * This is a minimal wrapper around a Queue for now, but can be extended
 * with priority, filtering, etc. in the future.
 */
public class CommandQueue {

    private final Queue<GameCommand> commands;

    public CommandQueue() {
        this.commands = new LinkedList<>();
    }

    /**
     * Add a command to the end of the queue.
     */
    public void enqueue(GameCommand command) {
        commands.add(command);
    }

    /**
     * Remove and return the next command, or null if empty.
     */
    public GameCommand dequeue() {
        return commands.poll();
    }

    /**
     * Check if the queue is empty.
     */
    public boolean isEmpty() {
        return commands.isEmpty();
    }

    /**
     * Get the number of commands in the queue.
     */
    public int size() {
        return commands.size();
    }

    /**
     * Remove all commands and return them as a list.
     */
    public List<GameCommand> drainAll() {
        List<GameCommand> result = new ArrayList<>(commands);
        commands.clear();
        return result;
    }

    /**
     * Clear all commands.
     */
    public void clear() {
        commands.clear();
    }

    @Override
    public String toString() {
        return String.format("CommandQueue{%d commands}", commands.size());
    }
}
