package com.group1.project3.repository;

import com.group1.project3.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagsRepository {

    /**
     * An array of sample tags.
     */
    public static final List<Tag> ITEMS = new ArrayList<>();

    static {
        // Add some sample items.
        addItem(new Tag("Hello"));
        addItem(new Tag("World"));
        addItem(new Tag("Test"));
    }

    private static void addItem(Tag item) {
        ITEMS.add(item);
    }
}