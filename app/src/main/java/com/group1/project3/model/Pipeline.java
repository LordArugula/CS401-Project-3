package com.group1.project3.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * A pipeline represents a category for tasks in a project.
 */
public class Pipeline implements SerializableAsMap {
    /**
     * The pipeline name.
     */
    private String name;

    /**
     * The cards in this pipeline.
     */
    private final HashSet<Card> cards;

    /**
     * Creates an empty pipeline with no name.
     */
    public Pipeline() {
        this("");
    }

    /**
     * Creates a pipeline with name.
     *
     * @param name The name of the pipeline.
     */
    public Pipeline(String name) {
        this.name = name;
        cards = new HashSet<>();
    }

    /**
     * Gets the name of the pipeline.
     *
     * @return the name of the pipeline.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the pipeline.
     *
     * @param name the name of the pipeline.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a read-only collection of the {@link Card cards} in this pipeline.
     *
     * @return a read-only collection of the {@link Card cards} in this pipeline.
     */
    public List<Card> getCards() {
        return Collections.unmodifiableList(new ArrayList<>(cards));
    }

    /**
     * Checks if a {@link Card} is in this pipeline.
     *
     * @param card The {@link Card} to search for.
     * @return True if the pipeline contains the {@link Card}.
     */
    public boolean hasCard(Card card) {
        return cards.contains(card);
    }

    /**
     * Adds a {@link Card} to this pipeline.
     *
     * @param card The {@link Card} to add.
     * @return True if the {@link Card} was added. False if it already is in this pipeline.
     */
    public boolean addCard(Card card) {
        return cards.add(card);
    }

    /**
     * Removes a {@link Card} from this pipeline.
     *
     * @param card The {@link Card} to remove.
     * @return True if the {@link Card} was removed from this pipeline.
     */
    public boolean removeCard(Card card) {
        return cards.remove(card);
    }

    /**
     * Moves a {@link Card} from this pipeline to the dest pipeline.
     * If the {@link Card} is not in this pipeline, the {@link Card}
     * is still added to dest.
     *
     * @param card The {@link Card} to move.
     * @param dest The destination pipeline
     * @return True if the card was moved.
     */
    public boolean moveCard(Card card, Pipeline dest) {
        if (hasCard(card)) {
            removeCard(card);
            return dest.addCard(card);
        }

        return dest.addCard(card);
    }

    public Map<String, Object> serializeAsMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);

        List<Map<String, Object>> cardsAsMapList = new ArrayList<>();
        for (Card card : cards) {
            cardsAsMapList.add(card.serializeAsMap());
        }

        map.put("cards", cardsAsMapList);
        return Collections.unmodifiableMap(map);
    }

    /**
     * Returns the number of cards in this pipeline.
     *
     * @return the number of cards in this pipeline.
     */
    public int size() {
        return cards.size();
    }
}
