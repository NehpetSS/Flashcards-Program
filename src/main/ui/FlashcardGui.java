package ui;

import model.CardSets;
import model.Event;
import model.EventLog;
import model.Flashcard;
import model.FlashcardSet;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;

// Made with reference from:
// https://docs.oracle.com/javase/tutorial/uiswing/components/button.html
// https://docs.oracle.com/javase/tutorial/uiswing/components/list.html
// https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
// https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html
// https://www.youtube.com/watch?v=ObVnyA8ar6Q
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// https://stackoverflow.com/questions/16622462/windowclosing-method

// Represents a GUI for a flashcard application
public class FlashcardGui extends JFrame implements ActionListener, WindowListener {
    private static final String JSON_STORE = "./data/cardsets.json";
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static final Dimension SCROLL_PANE_SIZE = new Dimension(WIDTH - 100, HEIGHT - 400);

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private CardSets cardSets;
    private JFrame selectionFrame;
    private JFrame flashcardsFrame;
    private JFrame studyFrame;
    private JPanel selectionPanel;
    private JTextField addText;
    private JTextField removeText;
    private JTextField selectSet;
    private DefaultListModel listModel;
    private DefaultListModel listModelFlashcards;
    private JTextField termText;
    private JTextField definitionText;
    private JTextField removeFlashcardText;
    private ImageIcon upload;
    private ImageIcon remove;

    // EFFECTS: constructor sets up cardsets and window
    public FlashcardGui() {
        cardSets = new CardSets();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        selectionPanel = new JPanel();
        initializeGraphics();
        setLayout(new GridBagLayout());

    }

    // MODIFIES: this
    // EFFECTS: draws the JFrame window and components where this FlashcardGui will operate
    public void initializeGraphics() {
        setTitle("Flashcard application");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        setSize(WIDTH, HEIGHT);
        termText = new JTextField(10);
        definitionText = new JTextField(10);
        removeFlashcardText = new JTextField(10);
        addText = new JTextField(10);
        removeText = new JTextField(10);
        selectSet = new JTextField(10);
        initializeImageIcons();

        add(mainMenuButtons());
        setLocationRelativeTo(null);
        setVisible(true);

        setSelectionPanel();
        setSelectionFrameButtons();
    }

    // MODIFIES: this
    // EFFECTS: imports and resizes image icons
    public void initializeImageIcons() {
        upload = new ImageIcon("data/+.png");
        Image i = upload.getImage();
        upload = new ImageIcon(i.getScaledInstance(18, 18, Image.SCALE_DEFAULT));
        remove = new ImageIcon("data/X.png");
        i = remove.getImage();
        remove = new ImageIcon(i.getScaledInstance(18, 18, Image.SCALE_DEFAULT));
    }

    // MODIFIES: this
    // EFFECTS: initializes selectionFrame and sets it to visible and sets main JFrame to be invisible
    public void displaySelectionFrame() {
        selectionFrame = new JFrame();
        selectionFrame.addWindowListener(this);
        selectionFrame.setTitle("Viewing Current Flashcard Sets");
        selectionFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        selectionFrame.setSize(WIDTH, HEIGHT);
        selectionFrame.setLocationRelativeTo(null);

        setVisible(false);
        selectionFrame.setVisible(true);

        selectionFrame.add(selectionPanel);
    }

    // MODIFIES: this
    // EFFECTS: sets selection frame to invisible while making main JFrame visible
    public void displayMain() {
        selectionFrame.setVisible(false);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: adds JScroll pane of FlashcardSets' titles to selectionPanel
    public void setSelectionPanel() {
        selectionPanel.add(setSelectionFrameList());
    }

    // MODIFIES: this
    // EFFECTS: creates and returns JScroll pane of the titles of all the FlashcardSets
    public JScrollPane setSelectionFrameList() {
        List<FlashcardSet> flashcardSets = cardSets.getCardSets();
        listModel = new DefaultListModel();

        for (FlashcardSet flashcardSet : flashcardSets) {
            listModel.addElement(flashcardSet.getTitle());
        }

        JList list = new JList(listModel);

        JScrollPane listScrollPane = new JScrollPane(list);
        listScrollPane.setPreferredSize(SCROLL_PANE_SIZE);

        return listScrollPane;
    }

    // MODIFIES: this
    // EFFECTS: adds needed buttons for the selectionPanel
    public void setSelectionFrameButtons() {
        JButton remove = new JButton("Remove Flashcard Set", this.remove);
        JButton add = new JButton("Add Flashcard Set", this.upload);
        JButton select = new JButton("Select Flashcard Set");
        JButton back = new JButton("Go Back To Main Menu");

        remove.addActionListener(this);
        add.addActionListener(this);
        back.addActionListener(this);
        select.addActionListener(this);

        remove.setActionCommand("remove set");
        add.setActionCommand("add set");
        select.setActionCommand("select set");
        back.setActionCommand("back to main");

        JPanel removePanel = new JPanel();
        JPanel addPanel = new JPanel();
        JPanel selectPanel = new JPanel();

        removePanel.add(remove);
        removePanel.add(removeText);
        addPanel.add(add);
        addPanel.add(addText);
        selectPanel.add(select);
        selectPanel.add(selectSet);

        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        addPanelsToSelectionPanel(addPanel, removePanel, selectPanel, back);
    }

    // MODIFIES: this
    // EFFECTS: helper for setSelectionFrameButtons() that adds specified elements to the selectionPanel
    public void addPanelsToSelectionPanel(JPanel addPanel, JPanel removePanel, JPanel selectPanel, JButton back) {
        selectionPanel.add(addPanel);
        selectionPanel.add(removePanel);
        selectionPanel.add(selectPanel);
        selectionPanel.add(back);
    }

    // MODIFIES: this
    // EFFECTS: creates and returns JPanel with the main menu buttons
    public JPanel mainMenuButtons() {
        JButton save = new JButton("Save Flashcard Sets");
        JButton load = new JButton("Load Flashcard Sets");
        JButton flashcardSets = new JButton("View Flashcard Sets");

        save.addActionListener(this);
        load.addActionListener(this);
        flashcardSets.addActionListener(this);

        save.setActionCommand("save");
        load.setActionCommand("load");
        flashcardSets.setActionCommand("view sets");

        JPanel panel = new JPanel();
        panel.add(save);
        panel.add(load);
        panel.add(flashcardSets);

        return panel;
    }

    // MODIFIES: this
    // EFFECTS: sets up JFrame for the given flashcardSet and sets selectionFrame to invisible
    public void displayFlashcardsFrame(FlashcardSet flashcardSet) {
        flashcardsFrame = new JFrame();
        flashcardsFrame.addWindowListener(this);
        flashcardsFrame.setTitle("Currently in " + flashcardSet.getTitle());
        flashcardsFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        flashcardsFrame.setSize(WIDTH, HEIGHT);
        flashcardsFrame.setLocationRelativeTo(null);

        selectionFrame.setVisible(false);
        flashcardsFrame.setVisible(true);

        flashcardsFrame.add(setFlashcardsPanel(flashcardSet));
    }

    // MODIFIES: this
    // EFFECTS: sets up JFrame to study the given flashcardSet and sets flashcardsFrame to invisible
    public void displayStudyFrame(FlashcardSet flashcardSet) {
        studyFrame = new JFrame();
        studyFrame.addWindowListener(this);
        studyFrame.setTitle("Currently studying " + flashcardSet.getTitle());
        studyFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        studyFrame.setSize(WIDTH, HEIGHT);
        studyFrame.setLocationRelativeTo(null);

        flashcardsFrame.setVisible(false);
        studyFrame.setVisible(true);

        studyFrame.add(setStudyFrame(flashcardSet));

    }

    // MODIFIES: this
    // EFFECTS: sets up JPanel for the studyFrame
    public JPanel setStudyFrame(FlashcardSet flashcardSet) {
        JPanel panel = new JPanel();
        JButton flip = new JButton("Flip Card");
        JButton next = new JButton("Next Card");
        JButton back = new JButton("Go Back to Editing");
        List<Flashcard> cards = makeFlashcardList(flashcardSet);
        JTextArea term = makeJTextArea(cards.get(0).getTerm());
        JTextArea definition = makeJTextArea(cards.get(0).getDefinition());
        cards.add(cards.get(0));
        cards.remove(0);

        definition.setVisible(false);

        back.setActionCommand("back to editing");
        back.addActionListener(this);

        addActionListenersForStudyPanel(next, flip, cards, term, definition);

        panel.add(back);
        panel.add(flip);
        panel.add(next);
        panel.add(setUpTermOrDefinitionPanel(term, definition));

        return panel;
    }

    // MODIFIES: this
    // EFFECTS: helper for setStudyFrame that adds actionListeners for components of studyFrame
    public void addActionListenersForStudyPanel(JButton next, JButton flip, List<Flashcard> cards,
                                                JTextArea term, JTextArea definition) {
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doNextCard(cards, term, definition);
            }
        });

        flip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flipCard(term, definition);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: sets text fields for the next card to be studied in the studyPanel
    public void doNextCard(List<Flashcard> cards, JTextArea term, JTextArea definition) {
        if (!cards.isEmpty()) {
            Flashcard nextCard = cards.get(0);
            cards.add(nextCard);
            cards.remove(0);
            term.setText(nextCard.getTerm());
            definition.setText(nextCard.getDefinition());
        }
    }

    // EFFECTS: creates and returns a copy of the flashcards that are in the given FlashcardSet
    public List<Flashcard> makeFlashcardList(FlashcardSet flashcardSet) {
        List<Flashcard> cards = new ArrayList<>();
        for (Flashcard flashcard : flashcardSet.getCardList()) {
            cards.add(flashcard);
        }
        return cards;
    }

    // EFFECTS: helper for setStudyFrame that sets up and returns the termOrDefinition panel
    public JPanel setUpTermOrDefinitionPanel(JTextArea term, JTextArea definition) {
        JPanel termOrDefinitionPanel = new JPanel();
        termOrDefinitionPanel.add(term);
        termOrDefinitionPanel.add(definition);
        termOrDefinitionPanel.setBorder(new LineBorder(Color.BLACK, 3, true));
        termOrDefinitionPanel.setPreferredSize(new Dimension(WIDTH - 100, HEIGHT - 300));
        return termOrDefinitionPanel;
    }

    // EFFECTS: helper for setStudyFrame that creates up and returns a formatted JTextArea
    public JTextArea makeJTextArea(String text) {
        JTextArea result = new JTextArea(text);
        result.setEditable(false);
        result.setLineWrap(true);
        result.setWrapStyleWord(true);
        result.setPreferredSize(new Dimension(WIDTH - 150, HEIGHT - 350));
        result.setOpaque(false);
        result.setFont(new Font("Arial",Font.BOLD,20));
        return result;
    }

    // EFFECTS: helper for setStudyFrame that 'flips' a flashcard by hiding its term and revealing its definition
    //          or hiding its definition and revealing its term
    public void flipCard(JTextArea term, JTextArea definition) {
        if (term.isVisible()) {
            term.setVisible(false);
            definition.setVisible(true);
        } else {
            definition.setVisible(false);
            term.setVisible(true);
        }
    }

    // MODIFIES: this
    // EFFECTS: creates and returns JPanel with information given the specified FlashcardSet
    public JPanel setFlashcardsPanel(FlashcardSet flashcardset) {
        JButton add = new JButton("Add Flashcard, Term then Definition", this.upload);
        JButton remove = new JButton("Remove Flashcard", this.remove);
        JButton back = new JButton("Go Back to Flashcard Selection Menu");
        JButton study = new JButton("Study Flashcards in Deck");

        add.setActionCommand("add flashcard");
        remove.setActionCommand("remove flashcard");
        back.setActionCommand("back to selection");

        addActionListenersForFlashcardPanel(flashcardset, add, remove, back, study);

        JPanel addPanel = new JPanel();
        JPanel removePanel = new JPanel();

        addPanel.add(add);
        addPanel.add(termText);
        addPanel.add(definitionText);
        removePanel.add(remove);
        removePanel.add(removeFlashcardText);

        return makeFlashcardsPanel(setFlashcardsFrameList(flashcardset), study, addPanel, removePanel, back);
    }

    // EFFECTS: helper for setFlashcardsPanel that sets up and returns a JPanel using the given components
    public JPanel makeFlashcardsPanel(JScrollPane scrollPane, JButton study, JPanel addPanel, JPanel removePanel,
                                      JButton back) {
        JPanel panel = new JPanel();
        panel.add(scrollPane);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(study);
        panel.add(addPanel);
        panel.add(removePanel);
        panel.add(back);

        return panel;
    }

    // MODIFIES: this
    // EFFECTS: helper for setFlashcardsPanel() that adds actionListeners for the given elements for
    //          the specified FlashcardSet
    public void addActionListenersForFlashcardPanel(FlashcardSet flashcardset, JButton add, JButton remove,
                                                    JButton back, JButton study) {
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFlashcard(flashcardset);
            }
        });
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeFlashcard(flashcardset);
            }
        });
        study.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!flashcardset.getCardList().isEmpty()) {
                    displayStudyFrame(flashcardset);
                }
            }
        });
        back.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: creates and returns JScrollPane containing the flashcards of the specified FlashcardSet
    public JScrollPane setFlashcardsFrameList(FlashcardSet flashcardSet) {
        listModelFlashcards = new DefaultListModel();

        for (Flashcard flashcard : flashcardSet.getCardList()) {
            listModelFlashcards.addElement("Term: " + flashcard.getTerm() + " | Definition: "
                    + flashcard.getDefinition());
        }

        JList list = new JList(listModelFlashcards);

        JScrollPane listScrollPane = new JScrollPane(list);
        listScrollPane.setPreferredSize(SCROLL_PANE_SIZE);

        return listScrollPane;
    }

    // MODIFIES: this
    // EFFECTS: remove FlashcardSet from cardSets if removeText.getText() is the title of an existing FlashcardSet
    //          and removes the title of that FlashcardSet from listModel
    public void removeFlashcardSet() {
        FlashcardSet remove = cardSets.findFlashcardSet(removeText.getText());
        if (cardSets.removeFlashcardSet(remove)) {
            listModel.removeElement(remove.getTitle());
        }
    }

    // MODIFIES: this
    // EFFECTS: add FlashcardSet to cardSets if addText.getText() is the title of a not already existing FlashcardSet
    //          and adds the title of that FlashcardSet to listModel
    public void addFlashcardSet() {
        FlashcardSet add = new FlashcardSet(addText.getText());
        if (!addText.getText().equals("") && cardSets.addFlashcardSet(add)) {
            listModel.addElement(add.getTitle());
        }
    }

    // MODIFIES: this
    // EFFECTS: if selectSet.getText() is the name of an existing FlashcardSet, then displayFlashcardsFrame with
    //          that FlashcardSet
    public void selectFlashcardSet() {
        FlashcardSet select = cardSets.findFlashcardSet(selectSet.getText());
        if (select != null) {
            displayFlashcardsFrame(select);
        }
    }

    // MODIFIES: this
    // EFFECTS: if a flashcard with the same term does not already exist in the set and if termText.getText() != ""
    //              creates and adds flashcard to the specified flashcard set using termText and definitionText fields
    //              to construct that flashcard and adds info from that flashcard to listModelFlashcards
    public void addFlashcard(FlashcardSet flashcardSet) {
        if (flashcardSet.findCard(termText.getText()) == null && !termText.getText().equals("")) {
            Flashcard newCard = new Flashcard(termText.getText(), definitionText.getText());
            flashcardSet.addCard(newCard);
            listModelFlashcards.addElement("Term: " + newCard.getTerm() + " | Definition: " + newCard.getDefinition());
        }
    }

    // MODIFIES: this
    // EFFECTS: if a flashcard with the term removeFlashcardText.getText() exists then
    //              remove that flashcard from the given FlashcardSet
    //              remove information of that flashcard from listModelFlashcards
    public void removeFlashcard(FlashcardSet flashcardSet) {
        Flashcard remove = flashcardSet.findCard(removeFlashcardText.getText());
        if (flashcardSet.removeCard(remove)) {
            listModelFlashcards.removeElement("Term: " + remove.getTerm() + " | Definition: "
                    + remove.getDefinition());
        }
    }

    // MODIFIES: this
    // EFFECTS: sets flashcardsFrame to invisible and displays the selectionFrame
    public void displaySelection() {
        flashcardsFrame.setVisible(false);
        selectionFrame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: sets studyFrame to invisible and displays the flashcardsFrame
    public void displayFlashcardsFromStudy() {
        flashcardsFrame.setVisible(true);
        studyFrame.setVisible(false);
    }

    // MODIFIES: this
    // EFFECTS: action handler calls a method depending on the ActionEvent given
    public void actionPerformed(ActionEvent e) {
        if ("save".equals(e.getActionCommand())) {
            saveCardSets();
        } else if ("load".equals(e.getActionCommand())) {
            loadCardSets();
        } else if ("view sets".equals((e.getActionCommand()))) {
            displaySelectionFrame();
        } else if ("add set".equals((e.getActionCommand()))) {
            addFlashcardSet();
        } else if ("back to main".equals((e.getActionCommand()))) {
            displayMain();
        } else if ("remove set".equals((e.getActionCommand()))) {
            removeFlashcardSet();
        } else if ("select set".equals((e.getActionCommand()))) {
            selectFlashcardSet();
        } else if ("back to selection".equals((e.getActionCommand()))) {
            displaySelection();
        } else if ("back to editing".equals((e.getActionCommand()))) {
            displayFlashcardsFromStudy();
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
            listModel.clear();
            List<FlashcardSet> flashcardSets = cardSets.getCardSets();
            for (FlashcardSet flashcardSet : flashcardSets) {
                listModel.addElement(flashcardSet.getTitle());
            }
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: window listener that prints events in the event log to console and quits the program
    //          when closing a window
    @Override
    public void windowClosing(WindowEvent e) {
        for (Event event : EventLog.getInstance()) {
            System.out.println(event);
        }
        System.exit(0);
    }

    // EFFECTS: does nothing when window is opened
    @Override
    public void windowOpened(WindowEvent e) {
    }

    // EFFECTS: does nothing when window is closed
    @Override
    public void windowClosed(WindowEvent e) {
    }

    // EFFECTS: does nothing when window is iconified
    @Override
    public void windowIconified(WindowEvent e) {
    }

    // EFFECTS: does nothing when window is deiconified
    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    // EFFECTS: does nothing when window is activated
    @Override
    public void windowActivated(WindowEvent e) {
    }

    // EFFECTS: does nothing when window is deactivated
    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
