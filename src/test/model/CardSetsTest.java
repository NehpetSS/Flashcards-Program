package model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardSetsTest {
    FlashcardSet fc1;
    FlashcardSet fc2;
    FlashcardSet fc3;

    CardSets testCardSets;

    @BeforeEach
    void runBefore() {
        fc1 = new FlashcardSet("fc");
        fc2 = new FlashcardSet("fc");
        fc3 = new FlashcardSet("fc3");

        testCardSets = new CardSets();


    }

    @Test
    void testConstructor() {
        assertTrue(testCardSets.getCardSets().isEmpty());
    }

    @Test
    void testAddFlashcardSet() {
        // add 1 to sets
        assertTrue(testCardSets.addFlashcardSet(fc1));
        assertEquals(1, testCardSets.getCardSets().size());
        assertEquals(fc1, testCardSets.getCardSets().get(0));

        // can't add (duplicate title)
        assertFalse(testCardSets.addFlashcardSet(fc2));
        assertEquals(1, testCardSets.getCardSets().size());
        assertEquals(fc1, testCardSets.getCardSets().get(0));

        // add another to sets
        assertTrue(testCardSets.addFlashcardSet(fc3));
        assertEquals(2, testCardSets.getCardSets().size());
        assertEquals(fc3, testCardSets.getCardSets().get(1));
    }

    @Test
    void testRemoveFlashcardSet() {
        testCardSets.addFlashcardSet(fc1);
        testCardSets.addFlashcardSet(fc3);

        // remove 1 from sets
        assertTrue(testCardSets.removeFlashcardSet(fc1));
        assertEquals(1, testCardSets.getCardSets().size());
        assertEquals(fc3, testCardSets.getCardSets().get(0));

        // can't remove because set with title does not exist
        assertFalse(testCardSets.removeFlashcardSet(fc2));
        assertEquals(1, testCardSets.getCardSets().size());
        assertEquals(fc3, testCardSets.getCardSets().get(0));

        // remove 1 and sets is empty
        assertTrue(testCardSets.removeFlashcardSet(fc3));
        assertEquals(0, testCardSets.getCardSets().size());
        assertTrue(testCardSets.getCardSets().isEmpty());

    }

    @Test
    void testFindFlashCardSet() {
        assertNull(testCardSets.findFlashcardSet("fc"));

        testCardSets.addFlashcardSet(fc1);
        testCardSets.addFlashcardSet(fc3);

        // FlashcardSet is found
        assertEquals(fc1, testCardSets.findFlashcardSet("fc"));

        // FlashcardSet is not in cardSets
        assertNull(testCardSets.findFlashcardSet("fc4"));
    }
}
