package ui.deprecated;

import model.CardSets;
import model.Flashcard;
import model.FlashcardSet;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Code influenced by Teller application https://github.students.cs.ubc.ca/CPSC210/TellerApp
// Code influenced by the JsonSerializationDemo https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// A Flashcard application
public class FlashcardApp {
    private static final String JSON_STORE = "./data/cardsets.json";
    private CardSets cardSets;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs flashcard application
    public FlashcardApp() {
        runFlashcardApp();
    }

    // MODIFIES: this
    // EFFECTS: processes the user's input
    private void runFlashcardApp() {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            displayMainMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("quit")) {
                keepGoing = false;
            } else {
                processMainMenuCommand(command);
            }

        }

        System.out.println("Closing Flashcard Program");

    }

    // MODIFIES: this
    // EFFECTS: initializes CardSets
    private void init() {
        cardSets = new CardSets();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: processes user commands
    private void processMainMenuCommand(String command) {
        if (command.equals("select")) {
            selectFlashcardSet();
        } else if (command.equals("add")) {
            addFlashcardSet();
        } else if (command.equals("rename")) {
            renameFlashcardSet();
        } else if (command.equals("remove")) {
            removeFlashcardSet();
        } else if (command.equals("save")) {
            saveCardSets();
        } else if (command.equals("load")) {
            loadCardSets();
        } else {
            System.out.println("Invalid Selection");
        }
    }

    // EFFECTS: displays options to user
    private void displayMainMenu() {
        System.out.println("\nSelect an Option:");
        System.out.println("\tselect - select a flashcard set");
        System.out.println("\tadd - add a flashcard set");
        System.out.println("\tremove - remove a flashcard set");
        System.out.println("\trename - select a flashcard set to rename");
        System.out.println("\tsave - save flashcard sets to file");
        System.out.println("\tload - load flashcard sets from file");
        System.out.println("\tquit - exit program");
    }

    // EFFECTS: prompts user to select a flashcard set to access
    private void selectFlashcardSet() {
        String setName;

        System.out.println("Current Flashcard Sets:");

        for (FlashcardSet flashcardSet : cardSets.getCardSets()) {
            System.out.println(flashcardSet.getTitle());
        }
        System.out.println("Type the name of the flashcard set you want to access");
        setName = input.next();

        if (cardSets.findFlashcardSet(setName) != null) {
            accessFlashcardSet(cardSets.findFlashcardSet(setName));
        } else {
            System.out.println("Could not find flashcard set with that name");
        }
    }

    // MODIFIES: this
    // EFFECTS: renames a flashcard set to specified title if another flashcard set does not share that title
    private void renameFlashcardSet() {
        String title;
        String newTitle;

        System.out.println("Current Flashcard Sets:");

        for (FlashcardSet flashcardSet : cardSets.getCardSets()) {
            System.out.println(flashcardSet.getTitle());
        }

        System.out.println("Type title of set to rename");
        title = input.next();
        if (cardSets.findFlashcardSet(title) == null) {
            System.out.println("Could not find a flashcard set with given title");
        } else {
            System.out.println("Enter new title");
            newTitle = input.next();
            if (newTitle.equals("")) {
                System.out.println("Flashcard set title can't be empty");
            } else {
                cardSets.findFlashcardSet(title).setTitle(newTitle);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user to add a flashcard set with given title
    private void addFlashcardSet() {
        String title;

        System.out.println("Enter the name of the flashcard set you want to add");
        title = input.next();

        if (!title.equals("")) {
            if (cardSets.addFlashcardSet(new FlashcardSet(title))) {
                System.out.println("Successfully added flashcard set");
            } else {
                System.out.println("Couldn't create flashcard set because set with title already exists");
            }
        } else {
            System.out.println("Can't create flashcard set with an empty title");
        }
    }

    // MODIFIES: this
    // EFFECTS: removes flashcard set from card sets if it exists
    private void removeFlashcardSet() {
        String title;

        System.out.println("Current Flashcard Sets:");

        for (FlashcardSet flashcardSet : cardSets.getCardSets()) {
            System.out.println(flashcardSet.getTitle());
        }

        System.out.println("Enter name of flashcard set you want to remove");
        title = input.next();

        if (cardSets.removeFlashcardSet(cardSets.findFlashcardSet(title))) {
            System.out.println("Flashcard set successfully removed");
        } else {
            System.out.println("Could not find flashcard set to remove");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user's input in a specific flashcardSet
    private void accessFlashcardSet(FlashcardSet flashcardSet) { // method based on code from teller application
        boolean insideSet = true;
        String command;

        while (insideSet) {
            displayFlashcardSetMenu(flashcardSet);
            command = input.next();

            if (command.equals("back")) {
                insideSet = false;
            } else {
                processFlashcardSetMenuCommand(flashcardSet, command);
            }
        }
    }

    // EFFECTS: displays options to user and cards currently in deck
    private void displayFlashcardSetMenu(FlashcardSet flashcardSet) { // method based on code from teller application
        System.out.println("\nFlashcards in the deck:");
        for (Flashcard flashcard : flashcardSet.getCardList()) {
            System.out.println("\n\tTerm: " + flashcard.getTerm());
            System.out.println("\tDefinition: " + flashcard.getDefinition());
        }

        System.out.println("\nFlashcards in the high priority deck:");
        for (Flashcard flashcard : flashcardSet.getHighPriorityList()) {
            System.out.println("\n\tTerm: " + flashcard.getTerm());
            System.out.println("\tDefinition: " + flashcard.getDefinition());
        }

        System.out.println("\nSelect an Option:");
        System.out.println("\tstudy - study flashcards");
        System.out.println("\tadd - add a flashcard");
        System.out.println("\tremove - remove a flashcard");
        System.out.println("\tedit - edit a flashcard");
        System.out.println("\tadd hp - add a flashcard in the deck to high priority cards");
        System.out.println("\tremove hp - remove a flashcard from high priority cards");
        System.out.println("\tback - go back to main menu");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processFlashcardSetMenuCommand(FlashcardSet flashcardSet, String command) {
        if (command.equals("add")) {              // method based on code from teller application
            addFlashcard(flashcardSet);
        } else if (command.equals("remove")) {
            removeFlashcard(flashcardSet);
        } else if (command.equals("edit")) {
            editFlashcard(flashcardSet);
        } else if (command.equals("add hp")) {
            addFlashcardToHighPriority(flashcardSet);
        } else if (command.equals("remove hp")) {
            removeFlashcardFromHighPriority(flashcardSet);
        } else if (command.equals("study")) {
            accessStudyMode(flashcardSet);
        } else {
            System.out.println("invalid option");
        }
    }

    // EFFECTS: processes user input while studying in a flashcard set
    private void accessStudyMode(FlashcardSet flashcardSet) {
        boolean studying = true;                    // method based on code from teller application
        String command;

        while (studying) {
            displayStudyModeMenu();
            command = input.next();

            if (command.equals("back")) {
                studying = false;
            } else {
                processStudyModeMenuCommand(flashcardSet, command);
            }
        }
    }

    // EFFECTS: displays options to the user when studying
    private void displayStudyModeMenu() {           // method based on code from teller application
        System.out.println("\nSelect an Option:");
        System.out.println("\tall - study using all cards in the deck");
        System.out.println("\tpriority - study only the cards in the high priority deck");
        System.out.println("\tback - go back to previous menu");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processStudyModeMenuCommand(FlashcardSet flashcardSet, String command) {
        if (command.equals("all")) {
            studyAll(flashcardSet);
        } else if (command.equals("priority")) {
            studyPriority(flashcardSet);
        } else {
            System.out.println("invalid option");
        }
    }

    // EFFECTS: allows user to go through each term in deck and flipping the card
    private void studyAll(FlashcardSet flashcardSet) {
        String done;

        System.out.println("Enter to see definition/Go to next card");
        System.out.println("Type done to go back");
        for (Flashcard card : flashcardSet.getCardList()) {
            System.out.println("\tTerm: " + card.getTerm());
            done = input.next();
            if (done.equals("done")) {
                break;
            }
            System.out.println("\tDefinition: " + card.getDefinition());
            done = input.next();
            if (done.equals("done")) {
                break;
            }
        }
    }

    // EFFECTS: allows user to go through each term in deck and flipping the card
    private void studyPriority(FlashcardSet flashcardSet) {
        String done;

        System.out.println("Enter to see definition/Go to next card");
        System.out.println("Type done to go back");
        for (Flashcard card : flashcardSet.getHighPriorityList()) {
            System.out.println("\tTerm: " + card.getTerm());
            done = input.next();
            if (done.equals("done")) {
                break;
            }
            System.out.println("\tDefinition: " + card.getDefinition());
            done = input.next();
            if (done.equals("done")) {
                break;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: removes flashcard from high priority flashcards if found
    private void removeFlashcardFromHighPriority(FlashcardSet flashcardSet) {
        String term;

        System.out.println("Select term of the card to remove from high priority");
        term = input.next();

        if (flashcardSet.removeCardFromHighPriority(flashcardSet.findCard(term))) {
            System.out.println("Successfully removed card from high priority");
        } else {
            System.out.println("Could not find card to be removed from high priority");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds flashcard to high priority list if exists and is not already in high priority flashcards
    private void addFlashcardToHighPriority(FlashcardSet flashcardSet) {
        String term;

        System.out.println("Select term of card to add to high priority");
        term = input.next();

        if (!flashcardSet.getCardList().isEmpty() && flashcardSet.addCardToHighPriority(flashcardSet.findCard(term))) {
            System.out.println("Successfully added card to high priority");
        } else {
            System.out.println("Could not add card to high priority,"
                    + "high priority already contains card or card is not in the main deck");
        }
    }

    // MODIFIES: this
    // EFFECTS: edit an existing flashcard
    //          if new term specified already exists on another card, does not edit the term
    private void editFlashcard(FlashcardSet flashcardSet) {
        String term;
        String newTerm;
        String newDefinition;
        Flashcard cardToBeEdited;

        System.out.println("Enter term of flashcard to be edited");
        term = input.next();
        cardToBeEdited = flashcardSet.findCard(term);

        if (cardToBeEdited == null) {
            System.out.println("Could not find that flashcard");
        } else {
            System.out.println("Enter new term or nothing to keep it the same");
            newTerm = input.next();
            if (flashcardSet.findCard(newTerm) != null) {
                System.out.println("Could not edit card as card already exists with this term");
            } else {
                System.out.println("Enter new definition or nothing to keep it the same");
                newDefinition = input.next();

                cardToBeEdited.editCard(newTerm, newDefinition);
                System.out.println("Card successfully updated");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: creates and adds flashcard to the flashcard set
    private void addFlashcard(FlashcardSet flashcardSet) {
        String term;
        String definition;

        System.out.println("Enter term:");
        term = input.next();

        if (!term.equals("")) {
            System.out.println("Enter definition:");
            definition = input.next();

            if (flashcardSet.addCard(new Flashcard(term, definition))) {
                System.out.println("Successfully added flashcard");
            } else {
                System.out.println("Could not add flashcard because flashcard with that "
                        + "term already exists in the deck");
            }
        } else {
            System.out.println("Could not add flashcard because term can't be empty");
        }
    }

    // MODIFIES: this
    // EFFECTS: removes flashcard from flashcard set and high priority list if it exists
    private void removeFlashcard(FlashcardSet flashcardSet) {
        String term;

        System.out.println("Enter term name of flashcard to be removed");
        term = input.next();

        if (flashcardSet.findCard(term) != null) {
            flashcardSet.removeCardFromHighPriority(flashcardSet.findCard(term));
            flashcardSet.removeCard(flashcardSet.findCard(term));
            System.out.println("Card successfully removed");
        } else {
            System.out.println("Could not find card with that term, no card removed");
        }
    }

    // EFFECTS: saves CardSets to file
    private void saveCardSets() {
        try {
            jsonWriter.open();
            jsonWriter.write(cardSets);
            jsonWriter.close();
            System.out.println("Saved " + "flashcard sets" + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads CardSets from file
    private void loadCardSets() {
        try {
            cardSets = jsonReader.read();
            System.out.println("Loaded " + "flashcard sets" + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
