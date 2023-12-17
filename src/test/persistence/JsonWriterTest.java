
package persistence;

import model.CardSets;
import model.Flashcard;
import model.FlashcardSet;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Code influenced by the JsonSerializationDemo https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            CardSets cs = new CardSets();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            CardSets cs = new CardSets();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyCardSets.json");
            writer.open();
            writer.write(cs);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyCardSets.json");
            cs = reader.read();
            assertTrue(cs.getCardSets().isEmpty());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            CardSets cs = new CardSets();

            FlashcardSet set1 = new FlashcardSet("set1");
            Flashcard t1 = new Flashcard("t1", "d1");
            Flashcard t2 = new Flashcard("t2", "d2");
            set1.addCard(t1);
            set1.addCard(t2);
            set1.addCardToHighPriority(t1);

            FlashcardSet set2 = new FlashcardSet("set2");
            set2.addCard(new Flashcard("t1", "d1"));

            cs.addFlashcardSet(set1);
            cs.addFlashcardSet(set2);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralCardSets.json");
            writer.open();
            writer.write(cs);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralCardSets.json");
            cs = reader.read();
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
            fail("Exception should not have been thrown");
        }
    }
}

