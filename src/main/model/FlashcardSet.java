package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// code from JsonSerializationDemo https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a set of multiple flashcards and flashcards that need to be prioritized for studying more
public class FlashcardSet {
    private List<Flashcard> cardList;
    private List<Flashcard> highPriorityList;
    private String title;

    // REQUIRES: title cannot be ""
    // EFFECTS: create a new set of flashcards with no flashcards
    //          in the set or in the highPriorityList and a given title
    public FlashcardSet(String title) {
        this.title = title;
        this.cardList = new ArrayList<>();
        this.highPriorityList = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: if cardList does not already contain a flashcard with the same term
    //              adds flashcard to cardList and returns true
    //          otherwise return false
    public Boolean addCard(Flashcard flashcard) {
        for (Flashcard card : cardList) {
            if (flashcard.getTerm().equals(card.getTerm())) {
                return false;
            }
        }
        cardList.add(flashcard);
        EventLog.getInstance().logEvent(new Event("Flashcard: [Term: " + flashcard.getTerm()
                + " Definition: " + flashcard.getDefinition() + "] added to set: [" + title + "]"));
        return true;
    }

    // MODIFIES: this
    // EFFECTS: if flashcard is found in cardList, remove it from cardList and return true
    //          otherwise return false
    public Boolean removeCard(Flashcard flashcard) {
        if (cardList.remove(flashcard)) {
            EventLog.getInstance().logEvent(new Event("Flashcard: [Term: " + flashcard.getTerm()
                    + " Definition: " + flashcard.getDefinition() + "] removed from set: [" + title + "]"));
            return true;

        }
        return false;
    }

    // REQUIRES: flashcard must be in cardList
    // MODIFIES: this
    // EFFECTS: if the flashcard is not already in highPriorityList
    //              add the flashcard to highPriorityList and return true
    //          otherwise return false
    public Boolean addCardToHighPriority(Flashcard flashcard) {
        if (!highPriorityList.contains(flashcard)) {
            return highPriorityList.add(flashcard);
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: if highPriorityList contains flashcard
    //              remove flashcard from highPriorityList and return true
    //          otherwise return false
    public Boolean removeCardFromHighPriority(Flashcard flashcard) {
        return highPriorityList.remove(flashcard);
    }

    // EFFECTS: if flashcard with specified term is found return that flashcard
    //          otherwise return null
    public Flashcard findCard(String term) {
        for (Flashcard card : cardList) {
            if (term.equals(card.getTerm())) {
                return card;
            }
        }
        return null;
    }

    // REQUIRES: title != ""
    // MODIFIES: this
    // EFFECTS: sets the title to a different name
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public List<Flashcard> getCardList() {
        return cardList;
    }

    public List<Flashcard> getHighPriorityList() {
        return highPriorityList;
    }


    // EFFECTS: returns this as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("cardList", flashcardsToJson(cardList));
        json.put("highPriorityList", flashcardsToJson(highPriorityList));
        return json;
    }

    // EFFECTS: returns flashcards in this flashcardSet as a JSON array
    private JSONArray flashcardsToJson(List<Flashcard> list) {
        JSONArray jsonArray = new JSONArray();

        for (Flashcard f : list) {
            jsonArray.put(f.toJson());
        }

        return jsonArray;
    }
}
