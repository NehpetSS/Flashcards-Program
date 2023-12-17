package persistence;

import model.CardSets;
import model.Flashcard;
import model.FlashcardSet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Code influenced by the JsonSerializationDemo https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a reader that reads cardSets from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads cardSets from file and returns it;
    // throws IOException if an error occurs reading data from file
    public CardSets read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseCardSets(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses CardSets from JSON object and returns it
    private CardSets parseCardSets(JSONObject jsonObject) {
        CardSets cs = new CardSets();
        addFlashcardSets(cs, jsonObject);
        return cs;
    }

    // MODIFIES: cs
    // EFFECTS: parses FlashcardSets from JSON object and adds them to CardSets
    private void addFlashcardSets(CardSets cs, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("cardSets");
        for (Object json : jsonArray) {
            JSONObject nextFlashcardSet = (JSONObject) json;
            addFlashcardSet(cs, nextFlashcardSet);
        }
    }

    // MODIFIES: cs
    // EFFECTS: parses flashcardSet from JSON object and adds it to CardSets
    private void addFlashcardSet(CardSets cs, JSONObject jsonObject) {
        String title = jsonObject.getString("title");
        JSONArray jsonCardListArray = jsonObject.getJSONArray("cardList");
        JSONArray jsonHighPriorityListArray = jsonObject.getJSONArray("highPriorityList");
        FlashcardSet flashcardSet = new FlashcardSet(title);

        for (Object json : jsonCardListArray) {
            JSONObject nextFlashcard = (JSONObject) json;
            addFlashcard(flashcardSet, nextFlashcard);
        }
        for (Object json : jsonHighPriorityListArray) {
            JSONObject nextFlashcard = (JSONObject) json;
            addFlashcardHP(flashcardSet, nextFlashcard);
        }

        cs.addFlashcardSet(flashcardSet);
    }

    // MODIFIES: flashcardSet
    // EFFECTS: parses flashcard from JSON object and adds it to flashcardSet
    private void addFlashcard(FlashcardSet flashcardSet, JSONObject jsonObject) {
        String term = jsonObject.getString("term");
        String definition = jsonObject.getString("definition");
        Flashcard flashcard = new Flashcard(term, definition);

        flashcardSet.addCard(flashcard);
    }

    // MODIFIES: flashcardSet
    // EFFECTS: parses flashcard from JSON object and adds it to high priority in flashcardSet
    private void addFlashcardHP(FlashcardSet flashcardSet, JSONObject jsonObject) {
        String term = jsonObject.getString("term");
        flashcardSet.addCardToHighPriority(flashcardSet.findCard(term));
    }
}
