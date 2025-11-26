package com.shatteredpixel.engine.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple collector for GameEvent objects produced during a tick.
 *
 * The engine publishes events to this collector as they occur,
 * and clients can drain all events at the end of the tick.
 */
public class EventCollector {

    private final List<GameEvent> events;

    public EventCollector() {
        this.events = new ArrayList<>();
    }

    /**
     * Publish an event.
     */
    public void publish(GameEvent event) {
        events.add(event);
    }

    /**
     * Get all events (immutable view).
     */
    public List<GameEvent> getAll() {
        return Collections.unmodifiableList(events);
    }

    /**
     * Remove all events and return them as a list.
     */
    public List<GameEvent> drainAll() {
        List<GameEvent> result = new ArrayList<>(events);
        events.clear();
        return result;
    }

    /**
     * Clear all events.
     */
    public void clear() {
        events.clear();
    }

    /**
     * Get the number of events.
     */
    public int size() {
        return events.size();
    }

    /**
     * Check if there are no events.
     */
    public boolean isEmpty() {
        return events.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("EventCollector{%d events}", events.size());
    }
}
