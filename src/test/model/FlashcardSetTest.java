package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FlashcardSetTest {
    FlashcardSet testSet;
    Flashcard c1;
    Flashcard c2;
    Flashcard c3;
    Flashcard c4;

    @BeforeEach
    void runBefore() {
        testSet = new FlashcardSet("testSet");
        c1 = new Flashcard("A", "D");
        c2 = new Flashcard("A", "D1");
        c3 = new Flashcard("B", "D2");
        c4 = new Flashcard("C", "D2");
    }

    @Test
    void testConstructor() {
        assertEquals("testSet", testSet.getTitle());
        assertTrue(testSet.getCardList().isEmpty());
        assertTrue(testSet.getHighPriorityList().isEmpty());
    }

    @Test
    void testAddCard() {
        // 1 flashcard added
        assertTrue(testSet.addCard(c1));
        assertEquals(1, testSet.getCardList().size());
        assertEquals(c1, testSet.getCardList().get(0));

        // 2 flashcards added
        assertTrue(testSet.addCard(c3));
        assertEquals(2, testSet.getCardList().size());
        assertEquals(c3, testSet.getCardList().get(1));

        // can't add flashcard (duplicate terms)
        assertFalse(testSet.addCard(c2));
        assertEquals(2, testSet.getCardList().size());
        assertEquals(c3, testSet.getCardList().get(1));

    }

    @Test
    void testRemoveCard() {
        testSet.addCard(c1);
        testSet.addCard(c3);

        // not in card list
        assertFalse(testSet.removeCard(c4));

        // card in list
        assertTrue(testSet.removeCard(c1));
        assertEquals(1, testSet.getCardList().size());
        assertEquals(c3, testSet.getCardList().get(0));

        // no cards left
        assertTrue(testSet.removeCard(c3));
        assertTrue(testSet.getCardList().isEmpty());
    }

    @Test
    void testAddCardToHighPriority() {
        testSet.addCard(c1);
        testSet.addCard(c3);

        // empty
        assertTrue(testSet.addCardToHighPriority(c1));
        assertEquals(1, testSet.getHighPriorityList().size());
        assertEquals(c1, testSet.getHighPriorityList().get(0));

        // can't add since already exists
        assertFalse(testSet.addCardToHighPriority(c1));
        assertEquals(1, testSet.getHighPriorityList().size());
        assertEquals(c1, testSet.getHighPriorityList().get(0));


        // 2 in list
        assertTrue(testSet.addCardToHighPriority(c3));
        assertEquals(2, testSet.getHighPriorityList().size());
        assertEquals(c3, testSet.getHighPriorityList().get(1));
    }

    @Test
    void testRemoveCardFromHighPriority() {
        testSet.addCard(c1);
        testSet.addCard(c3);
        testSet.addCardToHighPriority(c1);
        testSet.addCardToHighPriority(c3);

        // not in list
        assertFalse(testSet.removeCardFromHighPriority(c4));
        assertEquals(2, testSet.getHighPriorityList().size());

        // remove 1 from list
        assertTrue(testSet.removeCardFromHighPriority(c1));
        assertEquals(1, testSet.getHighPriorityList().size());
        assertEquals(c3, testSet.getHighPriorityList().get(0));

        // remove another, list is empty
        assertTrue(testSet.removeCardFromHighPriority(c3));
        assertTrue(testSet.getHighPriorityList().isEmpty());

    }
    @Test
    void testFindCard() {
        // empty card list
        assertNull(testSet.findCard("A"));

        testSet.addCard(c1);
        testSet.addCard(c3);

        // card is found
        assertEquals(c1, testSet.findCard("A"));

        // card is not in card list
        assertNull(testSet.findCard("C"));
    }
    @Test
    void testSetTitle() {
        testSet.setTitle("New Title");
        assertEquals("New Title", testSet.getTitle());
    }
}
