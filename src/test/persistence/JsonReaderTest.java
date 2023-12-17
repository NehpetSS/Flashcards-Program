package persistence;

import model.CardSets;
import model.FlashcardSet;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Code influenced by the JsonSerializationDemo https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            CardSets cs = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyCardSets() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyCardSets.json");
        try {
            CardSets cs = reader.read();
            assertTrue(cs.getCardSets().isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralCardSets() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralCardSets.json");
        try {
            CardSets cs = reader.read();
            List<FlashcardSet> flashcardSets = cs.getCardSets();
            assertEquals(2, cs.getCardSets().size());

            // check 1st set
            assertEquals(2, flashcardSets.get(0).getCardList().size());
            assertEquals(1, flashcardSets.get(0).getHighPriorityList().size());
            checkFlashcard("t1", "d1", flashcardSets.get(0).getCardList().get(0));
            checkFlashcard("t2", "d2", flashcardSets.get(0).getCardList().get(1));
            checkFlashcard("t1", "d1", flashcardSets.get(0).getHighPriorityList().get(0));

            // check 2nd set
            assertEquals(1, flashcardSets.get(1).getCardList().size());
            assertTrue(flashcardSets.get(1).getHighPriorityList().isEmpty());
            checkFlashcard("t1", "d1", flashcardSets.get(1).getCardList().get(0));

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
