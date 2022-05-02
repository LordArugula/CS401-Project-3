package com.group1.project3.model;

import java.util.Objects;

/**
 * Tags are labels that can be added to {@link Card cards}.
 */
public class Tag {
    /**
     * The tag name.
     */
    private String name;

    /**
     * The tag color.
     */
    private int color;

    public Tag() {
        this(null);
    }

    /**
     * Creates a tag with name with a default white color.
     *
     * @param name The name of the tag.
     */
    public Tag(String name) {
        this(name, -1);
    }

    /**
     * Creates a tag with name and color.
     *
     * @param name  The name of the tag.
     * @param color The color of the tag.
     */
    public Tag(String name, int color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Gets the name of the tag.
     *
     * @return the name of the tag.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the tag.
     *
     * @param name the name of the tag.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the color of the tag.
     *
     * @return the color of the tag.
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets the color of the tag.
     *
     * @param color the color of the tag.
     */
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return color == tag.color && name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
}
