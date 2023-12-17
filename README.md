# Flashcard Project

## Description
A flashcard program which will allow the creation and saving of multiple sets of flashcards.
The application will have features that allow users create/modify/delete flashcards.
My program is meant to be used by users who want a free flashcard program that is easy to use.
This project is of interest to me because I'll find it to be a helpful study tool to since this
will only have the features that I find useful. It also interests me because I'll have the ability
to add more features if I need them.



### User Stories
- As a user, I want to be able to create a flashcard and add it to a flashcard set (list of flashcards)
- As a user, I want to be able to view the titles of my flashcard sets
- As a user, I want to be able to view a list of all my flashcards currently in a flashcard set
- As a user, I want to be able to create flashcard with a term (front) and description (back)
- As a user, I want to be able to modify and delete existing flashcards
- As a user, I want to be able to add certain flashcards to a high priority study list
- As a user, I want to be able to save my flashcard sets to file
- As a user, I want to be able to load my flashcard sets from file

### Instructions for Grader
- You can generate the first required action related to the first user story of adding multiple Flashcards to a flashcard set (Add Xs to Y) by
  1) clicking 'View FlashcardSets'
  2) creating a flashcard set with a given name by typing that name into the text box beside the 'Add Flashcard Set' button and then pressing the button
  3) then type the name of the flashcard set you just made beside the 'select flashcard set' button and press it to go into it
  4) Now to add a flashcard, beside the 'add flashcard' button are 2 text boxes, the one on
     the left is the term of the flashcard and the one on the right is its definition. After typing in the term and definition of the flashcard you want to create, click the button to add the flashcard to the set
  5) the flashcard you just made (and every other flashcard in that set already) will be displayed on the screen (View all Xs in Y).

- The second required action related to the first user story is the ability to remove cards from a flashcard set (Remove Xs from Y) can be achieved after doing the first required action of creating a flashcard.
You can do this by typing the term of the flashcard into that you want to remove into the text box beside the button 'Remove Flashcard' and then clicking the button. The specified flashcard will be removed from that flashcard set and will disappear from the display
- You can save the state of the application by using the buttons to navigate back to the main menu and clicking the 'save flashcard sets' button
- You can load the state of the application by using the buttons to navigate back to the main menu and clicking the 'load flashcard sets' button
- My visual component can be found by starting from the main menu and clicking the 'view flashcard sets' button. There are 2 buttons with pictures here, the 'add flashcard set' button has a picture of an arrow and the 'remove flashcard set' button has a picture of an X.

### Phase 4: Task 2

A sample log of events:

- Fri Dec 01 16:53:34 PST 2023

- Flashcard Set: [set1] added

- Fri Dec 01 16:53:36 PST 2023

- Flashcard Set: [set2] added

- Fri Dec 01 16:53:38 PST 2023

- Flashcard Set: [set2] removed

- Fri Dec 01 16:53:45 PST 2023

- Flashcard: [Term: t1 Definition: d1] added to set: [set1]

- Fri Dec 01 16:53:49 PST 2023

- Flashcard: [Term: t2 Definition: d2] added to set: [set1]

- Fri Dec 01 16:53:53 PST 2023

- Flashcard: [Term: t3 Definition: d3] added to set: [set1]

- Fri Dec 01 16:53:55 PST 2023

- Flashcard: [Term: t3 Definition: d3] removed from set: [set1]

### Phase 4: Task 3

If I had more time to work on the project I would refactor my CardSets class using
the singleton design pattern. I could change it to a singleton because I only ever utilize 1 and there should only be 1
CardSets class which is in the FlashcardGui class. I could throw exceptions for some of the methods instead of just producing
a boolean in the method implementations. For example, the method, removeCard() in my FlashcardSets class could throw a CardNotFoundException instead of producing a boolean.
Doing this for some my methods would make it easier to implement the FlashcardGui class by handling the exceptions there instead writing extra code to check if the boolean returned was true or false. Currently, 
my FlashcardGui class does too much (making the main menu JFrame, SelectionFrame JFrame, Flashcards JFrame, etc.). I think that separating some of the JFrames into their own
classes would increase the readability of the code and make it easier to understand.
