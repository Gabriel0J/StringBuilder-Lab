import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class HangmanGame implements ActionListener, KeyListener {
    private JFrame gameFrame;
    private JLabel[] wordLabels;
    private JLabel wrongAnswers;
    private String wrong_answers;
    private JLabel guessesLeft;
    private int guesses_left;
    private JLabel toGuess;
    private static String word;
    private static final String possibleInputs = "abcdefghijklmnopqrstuvwxyz";
    public static StringBuilder state = new StringBuilder();
    public String letterToGuess;
    public void go() {
        File f = new File("Expanded Word List");
        word = getWord(f);
        guesses_left = 7;
        wrong_answers = "";
        letterToGuess = "";
        initializeFrame();
        state.setLength(word.length());
        clearStrBuilder(state);
    }
    private static String getWord(File file) {
        ArrayList<String> listOfWords = new ArrayList<String>();
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNext()) {
                listOfWords.add(scan.nextLine().toLowerCase());
            }
        } catch (Exception e) {
            System.out.println("No file found! Exiting program");
            System.exit(1);
        }
        return listOfWords.get((int)(Math.random()*listOfWords.size()));
    }
    private void initializeFrame() {
        gameFrame = new JFrame();
        gameFrame.addKeyListener(this);
        gameFrame.setFocusable(true);
        gameFrame.setLayout(new GridLayout(4,1));

        initializeScreen();
        wrongAnswers = new JLabel("Incorrect Guesses: ", SwingConstants.CENTER);
        gameFrame.add(wrongAnswers);
        guessesLeft = new JLabel("Guesses Left: " + guesses_left, SwingConstants.CENTER);
        gameFrame.add(guessesLeft);
        toGuess = new JLabel("Guess: ", SwingConstants.CENTER);
        gameFrame.add(toGuess);

        gameFrame.setSize(500,200);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
    }
    private void initializeScreen() {
        JPanel gameScreen = new JPanel();
        gameScreen.setLayout(new GridLayout(1,word.length()));
        gameFrame.add(gameScreen);
        wordLabels = new JLabel[word.length()];
        for (int i = 0; i < wordLabels.length; i++) {
            JLabel label = new JLabel("",SwingConstants.CENTER);
            label.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,4));
            label.setFont(new Font("Dialog",Font.PLAIN,25));
            gameScreen.add(label);
            wordLabels[i] = label;
        }
    }
    private void clearStrBuilder(StringBuilder s) {
        for (int i = 0; i < s.length(); i++) {
            s.replace(i,i+1,"_");
        }
    }
    @Override public void actionPerformed(ActionEvent e) { //takes screen button input
        gameFrame.requestFocusInWindow();
    }
    @Override public void keyPressed(KeyEvent e) { //implements keyboard usability
        String input = e.getKeyText(e.getKeyCode());
        processInput(input.toLowerCase());
    }
    private void processInput(String input) {
        if (letterToGuess != null && input.equals("enter")) {
            compareAndOutput(letterToGuess);
        } else if (input.length() == 1 && possibleInputs.contains(input)) {
            letterToGuess = input;
            toGuess.setText("Guess: " + letterToGuess);
        }
    }
    private void compareAndOutput(String input) {
        if (state.indexOf(input) >= 0 || wrong_answers.contains(input)) {
            //System.out.println(state);
            //System.out.println(wrong_answers);
            toGuess.setText("Letter '" + input + "' has already been guessed!");
        } else if (word.contains(input)) {
            for (int i = 0; i < state.length(); i++) {
                if (word.substring(i, i + 1).equals(input)) {
                    state.replace(i, i+1, input);
                    wordLabels[i].setText(state.substring(i,i+1));
                }
            }
        } else {
                toGuess.setText("Letter '" + input + "' not in word!");
                guesses_left--;
                guessesLeft.setText("Guesses Left: " + guesses_left);
                wrong_answers = wrong_answers + " " + input;
                wrongAnswers.setText("Incorrect Guesses: " + wrong_answers);
        }
        if (state.toString().equals(word)) {
            gameComplete(Color.GREEN,"You Win!");
        }
        if (guesses_left == 0) {
            gameComplete(Color.RED,"You Lose!");
        }
    }
    public void gameComplete(Color color, String message) {
        for (int i = 0; i < word.length(); i++) {
            state.replace(i, i+1, word.substring(i,i+1));
            wordLabels[i].setText(state.substring(i,i+1));
            wordLabels[i].setBackground(color);
            wordLabels[i].setOpaque(true);
        }
        int query = JOptionPane.showConfirmDialog(null, message + " Play Again?", "Game Complete!",JOptionPane.YES_NO_OPTION);
        if (query == JOptionPane.YES_OPTION) {
            gameFrame.dispose();
            go();
        }
        if (query == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null,"Thank You For Playing!", "Goodbye!",JOptionPane.INFORMATION_MESSAGE);
            System.exit(1);
        }
    }

    @Override public void keyTyped(KeyEvent e) {
        //not used
    } // not used
    @Override public void keyReleased(KeyEvent e) {
        // not used
    } // not used

}