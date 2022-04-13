package com.group1.project3.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

public class Card {
    /**
     * The card id.
     */
    private final String id;

    /**
     * The card content.
     */
    private String content;

    /**
     * The tags on this card.
     */
    private final HashSet<Tag> tags;

    /**
     * The id of the user assigned to this card.
     */
    private String assignedUser;

    /**
     * The date assigned to this card.
     */
    private Date assignedDate;

    /**
     * Creates an empty card.
     *
     * @param id The id of the card.
     */
    public Card(String id) {
        this(id, null);
    }

    /**
     * Creates a card with a title and content.
     *
     * @param id      The id of the card.
     * @param content The content of the card.
     */
    public Card(String id, String content) {
        this.id = id;
        this.content = content;
        assignedDate = null;
        assignedUser = null;
        tags = new HashSet<>();
    }

    /**
     * Gets this card's id.
     *
     * @return this card's id.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets this card's title.
     *
     * @return this card's title.
     */
    public String getTitle() {
        return content.substring(0, content.indexOf('\n'));
    }

    /**
     * Gets this card's content.
     *
     * @return this card's content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Set this card's content.
     *
     * @param content The card's content.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the id of this card's assigned user.
     *
     * @return the id of this card's assigned user.
     */
    public String getAssignedUser() {
        return assignedUser;
    }

    /**
     * Assigns the user with userId to this card.
     *
     * @param userId The id of the user.
     */
    public void setAssignedUser(String userId) {
        assignedUser = userId;
    }

    /**
     * Returns the date that was assigned to this card.
     *
     * @return The date that was assigned to this card.
     */
    public Date getAssignedDate() {
        return assignedDate;
    }

    /**
     * Assigns a date to this card.
     *
     * @param date The date.
     */
    public void setAssignedDate(Date date) {
        assignedDate = date;
    }

    /**
     * Returns a read-only collection of this card's tags.
     *
     * @return a read-only collection of this card's tags.
     */
    public Collection<Tag> getTags() {
        return Collections.unmodifiableCollection(tags);
    }

    /**
     * Adds the tag to this card.
     *
     * @param tag The tag.
     * @return True if the tag was added to the card.
     */
    public boolean addTag(Tag tag) {
        return tags.add(tag);
    }

    /**
     * Checks if this card has tag.
     *
     * @param tag The tag.
     * @return True if this card has the tag.
     */
    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    /**
     * Removes the tag from this card.
     *
     * @param tag The tag.
     * @return True if the tag was removed from this card.
     */
    public boolean removeTag(String tag) {
        return tags.remove(tag);
    }
}
