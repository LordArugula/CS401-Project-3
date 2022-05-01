package com.group1.project3.model;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * test class for {@link Card} class
 */
public class CardTest extends TestCase {

    /**
     * tests the setters for id, content, user, and data and the getters for id, title, content, user, date, and tags
     */
    public void testSettersGetters() {
        String id = "123";
        String title = "title";
        String content = "title\ncontent";
        String empty = "";
        List<Tag> tags = new ArrayList<>();
        String assignedUser = "assignedUser";
        Date assignedDate = new Date(1 / 12);
        Card card = new Card();

        //test id
        card.setId(id);
        assertEquals(card.getId(), id);

        //test title and content
        card.setContent(empty);
        assertEquals(card.title(), empty);
        card.setContent(content);
        assertEquals(card.title(), title);
        assertEquals(card.getContent(), content);

        //test assigned user
        card.setAssignedUser(assignedUser);
        assertEquals(card.getAssignedUser(), assignedUser);

        //tests assigned date
        card.setAssignedDate(assignedDate);
        assertEquals(card.getAssignedDate(), assignedDate);

        //test tags
        assertEquals(card.getTags(), tags);
    }

    /**
     * tests addTag(), hasTag(), and removeTag()
     */
    public void testTagFunctions() {
        String id = "123";
        String name = "name";
        String name2 = "name2";
        List<Tag> tags = new ArrayList<>();
        Tag tag = new Tag(name);
        Tag tag2 = new Tag(name2);
        Card card = new Card(id);

        //test add tag
        assertTrue(card.addTag(tag));
        assertTrue(card.addTag(tag2));
        tags.add(tag);
        tags.add(tag2);
        assertEquals(card.getTags(), tags);

        //test has tag
        assertTrue(card.hasTag(tag));
        assertFalse(card.hasTag(new Tag("asdf")));

        //test remove tag
        assertTrue(card.removeTag(tag));
        tags.remove(tag);
        assertEquals(card.getTags(), tags);
    }
}