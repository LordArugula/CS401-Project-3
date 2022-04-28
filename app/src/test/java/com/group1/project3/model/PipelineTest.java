package com.group1.project3.model;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * test class for {@link Pipeline} class
 */
public class PipelineTest extends TestCase {

    /**
     * tests the setter for name and the getters for name/cards
     */
    public void testSettersGetters() {
        String name = "name";
        List<Card> cards = new ArrayList<>();
        Pipeline test = new Pipeline();

        test.setName(name);
        assertEquals(test.getName(), name);

        assertEquals(test.getCards(), cards);
    }

    /**
     * tests hasCard(), addCard(), removeCard(), moveCard(), and size()
     */
    public void testCardFunctions() {
        String name = "name";
        String name2 = "name2";
        List<Card> cards = new ArrayList<>();
        String id = "123";
        String id2 = "7654";
        Card card = new Card(id);
        Card card2 = new Card(id2);
        Pipeline test = new Pipeline(name);
        Pipeline test2 = new Pipeline(name2);

        assertEquals(test.getCards(), cards);

        assertTrue(test.addCard(card));
        assertTrue(test.addCard(card2));
        cards.add(card);
        cards.add(card2);
        assertEquals(test.getCards(), cards);

        assertEquals(test.size(), cards.size());

        assertTrue(test.hasCard(card));
        assertFalse(test.hasCard(new Card("asdf")));

        assertTrue(test.removeCard(card));
        cards.remove(card);
        assertEquals(test.getCards(), cards);

        assertTrue(test.moveCard(card2, test2));
        cards.remove(card2);
        assertEquals(test.getCards(), cards);
        assertTrue(test2.hasCard(card2));
        assertFalse(test2.hasCard(new Card("af")));

    }
}