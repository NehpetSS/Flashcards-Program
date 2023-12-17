package persistence;

import model.Flashcard;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Code influenced by the JsonSerializationDemo https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

public class JsonTest {
    protected void checkFlashcard(String term, String definition, Flashcard flashcard) {
        assertEquals(term, flashcard.getTerm());
        assertEquals(definition, flashcard.getDefinition());
    }
}
