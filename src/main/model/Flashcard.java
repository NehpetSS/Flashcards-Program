package model;

import org.json.JSONObject;

// code from JsonSerializationDemo https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a flashcard with a front (term) and backside (definition)
public class Flashcard {
    private String term;
    private String definition;

    // REQUIRES: term cannot be ""
    // EFFECTS: creates a new flashcard with given term and definition
    public Flashcard(String term, String definition) {
        this.term = term;
        this.definition = definition;
    }

    // MODIFIES: this
    // EFFECTS: set term to newTerm and definition to newDefinition
    //          if newTerm == "" term does not change
    //          if newDefinition == "" definition does not change
    public void editCard(String newTerm, String newDefinition) {
        if (!newTerm.equals("")) {
            term = newTerm;
        }
        if (!newDefinition.equals("")) {
            definition = newDefinition;
        }
    }

    public String getTerm() {
        return term;
    }

    public String getDefinition() {
        return definition;
    }

    // EFFECTS: returns this as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("term", term);
        json.put("definition", definition);
        return json;
    }

}
