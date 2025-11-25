package com.shatteredpixel.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple event bus for publishing and subscribing to game events.
 * Allows decoupling of game systems through an observer pattern.
 *
 * Design goals:
 * - Deterministic event ordering (FIFO processing)
 * - Type-safe event handling
 * - No reflection (GWT-compatible)
 * - Simple and fast
 *
 * Events are processed immediately (synchronous) to maintain determinism.
 * For delayed events, use the TickLoop's scheduled actions.
 */
public class EventBus {

    private final Map<Class<?>, List<EventListener<?>>> listeners;

    public EventBus() {
        this.listeners = new HashMap<>();
    }

    /**
     * Subscribe to events of a specific type.
     *
     * @param eventType The class of events to listen for
     * @param listener The listener to invoke when events occur
     */
    public <T> void subscribe(Class<T> eventType, EventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    /**
     * Unsubscribe a listener from a specific event type.
     */
    public <T> void unsubscribe(Class<T> eventType, EventListener<T> listener) {
        List<EventListener<?>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }
    }

    /**
     * Publish an event to all subscribed listeners.
     * Events are processed synchronously in subscription order (deterministic).
     *
     * @param event The event to publish
     */
    @SuppressWarnings("unchecked")
    public <T> void publish(T event) {
        List<EventListener<?>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            // Create a copy to avoid ConcurrentModificationException
            // if listeners modify subscriptions during event handling
            List<EventListener<?>> copy = new ArrayList<>(eventListeners);
            for (EventListener<?> listener : copy) {
                ((EventListener<T>) listener).onEvent(event);
            }
        }
    }

    /**
     * Clear all event listeners (used for engine reset).
     */
    public void clear() {
        listeners.clear();
    }

    /**
     * Functional interface for event listeners.
     */
    @FunctionalInterface
    public interface EventListener<T> {
        void onEvent(T event);
    }
}
