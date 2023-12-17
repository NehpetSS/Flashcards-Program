package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlashcardTest {
    private Flashcard testCard;

    @BeforeEach
    void runBefore() {
        testCard = new Flashcard("term", "definition");
    }

    @Test
    void testConstructor() {
        assertEquals("term", testCard.getTerm());
        assertEquals("definition", testCard.getDefinition());
    }

    @Test
    void testEditCard() {
        testCard.editCard("","");
        assertEquals("term", testCard.getTerm());
        assertEquals("definition", testCard.getDefinition());

        testCard.editCard("new term", "");
        assertEquals("new term", testCard.getTerm());
        assertEquals("definition", testCard.getDefinition());

        testCard.editCard("", "new definition");
        assertEquals("new term", testCard.getTerm());
        assertEquals("new definition", testCard.getDefinition());

        testCard.editCard("final term", "final definition");
        assertEquals("final term", testCard.getTerm());
        assertEquals("final definition", testCard.getDefinition());
    }
}