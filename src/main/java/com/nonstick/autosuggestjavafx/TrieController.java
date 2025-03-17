package com.nonstick.autosuggestjavafx;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.*;
import java.net.URL;
import java.util.*;

public class TrieController implements Initializable {
    @FXML
    private Label resultLabel;
    @FXML
    private Label corpusSizeLabel;
    @FXML
    private TextField generalTextField;
    @FXML
    private ComboBox<String> resultComboBox;
//    @FXML
//    private MouseEvent addButtonClick;
//    @FXML
//    private MouseEvent removeButtonCLick;
//    @FXML
//    private MouseEvent findButtonClick;
//    @FXML
//    private MouseEvent queryButtonClick;
    String[] something = {"whatever", "right"};
    String thing;

    private static TrieController instance;
    private Trie trie = new Trie();
    private final HashMap<String, Integer> wordCounts = new HashMap<>();
    private DirectedGraph graph =  new DirectedGraph();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //Trie autosuggest = new Trie();
        ArrayList<String>  words = new ArrayList<>();
        words.add("whatever");
        words.add("right");
        resultComboBox.getItems().addAll(words);
    }

    @FXML
    private void addTBLonClick(MouseEvent addTBLonClick) throws IOException {
        FileReader tbl = new FileReader("src/main/resources/documents/biglebowski.txt");
        BufferedReader br = new BufferedReader(tbl);
        String line = "";
        while (br.readLine() != null || !line.isEmpty()) {
            line = br.readLine();
            ArrayList<String> words = new ArrayList<>(Arrays.asList(line.split("\\s")));
            for(String word : words) {
                word = word.toLowerCase();
                if (isValidWord(word) && word.length() > 3) {
                    this.trie.insert(word);
                    if (wordCounts.containsKey(word)) {
                        wordCounts.put(word, wordCounts.get(word) + 1);
                    } else {
                        wordCounts.put(word, 1);
                    }
                }
            }
            corpusSizeLabel.setText(String.valueOf(trie.size()));
        }
        tbl.close();
        br.close();
    }

    @FXML
    private void addButtonClick(MouseEvent addButtonClick) {
        String word = generalTextField.getText();
        if(isValidWord(word)) {
            trie.insert(word);
            String result = "added word: " + word;
            System.out.println(result);
            resultLabel.setText(result);
            corpusSizeLabel.setText(String.valueOf(trie.size()));
        }
        generalTextField.clear();
    }

    private boolean isValidWord(String word) {
        boolean valid = false;
        if (!word.isEmpty()) {
            if(word.matches("^[a-zA-Z]*$")) {
                valid = true;
            } else {
                resultLabel.setText("String should only contain letters");
            }
        } else {
            resultLabel.setText("String should not be empty");
        }

        return valid;
    }

    @FXML
    private void removeButtonClick(MouseEvent removeButtonClick) {
        String word = generalTextField.getText();
        if(word != null && !word.isEmpty()) {
            if (trie.find(word, true)) {
                trie.delete(word, true);
                //System.out.println("removed word: " + word);
                resultLabel.setText("Removed Word: " + word);
                corpusSizeLabel.setText(String.valueOf(trie.size()));
            } else {
                resultLabel.setText(word + " not found in corpus");
            }
        }
        generalTextField.clear();
    }

    @FXML
    private void findButtonClick(MouseEvent findButtonClick) {
        String word = generalTextField.getText();
        if(word != null && !word.isEmpty()) {
            boolean found = trie.find(word, true);
            if(found) {
                //System.out.println("found word: " + word);
                resultLabel.setText("Found: " + word);
            } else {
                //System.out.println("Did not find word: " + word);
                resultLabel.setText("Not Found: " + word);
            }
        }
        generalTextField.clear();
    }

    @FXML
    private void queryButtonClick(MouseEvent queryButtonClick) {
        String word = generalTextField.getText();
        if(word != null && !word.isEmpty()) {
            ArrayList<String> queryResult = trie.suggest(word);
            resultLabel.setText("Query: " + word + " success.");
            resultComboBox.getItems().removeAll(resultComboBox.getItems());
            resultComboBox.getItems().addAll(queryResult);
            resultComboBox.getSelectionModel().selectFirst();
        } else {
            resultLabel.setText("Query: " + word + " failed.");
        }
        generalTextField.clear();
    }

}