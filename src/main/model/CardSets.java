package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// code from JsonSerializationDemo https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents multiple sets of flashcards
public class CardSets {
    private List<FlashcardSet> cardSets;

    // EFFECTS: creates a new set of flashcard sets with no flashcard sets in it
    public CardSets() {
        cardSets = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: if cardSets does not contain a FlashcardSet with a duplicate title
    //              adds flashcardSet to cardSets and returns true
    //          otherwise returns false
    public Boolean addFlashcardSet(FlashcardSet flashcardSet) {
        for (FlashcardSet set : cardSets) {
            if (set.getTitle().equals(flashcardSet.getTitle())) {
                return false;
            }
        }
        EventLog.getInstance().logEvent(new Event("Flashcard Set: [" + flashcardSet.getTitle()
                + "] added"));
        return cardSets.add(flashcardSet);
    }

    // MODIFIES: this
    // EFFECTS: if cardSets contains flashcardSet
    //              removes that FlashcardSet from cardSets and return true
    //          otherwise return false
    public Boolean removeFlashcardSet(FlashcardSet flashcardSet) {
        if (cardSets.remove(flashcardSet)) {
            EventLog.getInstance().logEvent(new Event("Flashcard Set: [" + flashcardSet.getTitle()
                    + "] removed"));
            return true;
        }
        return false;
    }

    // EFFECTS: if flashcard set with specified title is found return that flashcard set
    //          otherwise return null
    public FlashcardSet findFlashcardSet(String title) {
        for (FlashcardSet flashcardSet : cardSets) {
            if (title.equals(flashcardSet.getTitle())) {
                return flashcardSet;
            }
        }
        return null;
    }

    public List<FlashcardSet> getCardSets() {
        return cardSets;
    }

    // EFFECTS: returns this as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("cardSets", flashcardSetsToJson());
        return json;
    }

    // EFFECTS: returns flashcardSets in this cardSets as a JSON array
    private JSONArray flashcardSetsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (FlashcardSet flashcardSet : cardSets) {
            jsonArray.put(flashcardSet.toJson());
        }

        return jsonArray;
    }
}
