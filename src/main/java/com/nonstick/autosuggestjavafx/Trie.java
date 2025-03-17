package com.nonstick.autosuggestjavafx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    public class TrieNode {
        Map<Character, TrieNode> children;
        char c;
        boolean isWord;

        public TrieNode(char c) {
            this.c = c;
            children = new HashMap<>();
        }

        public TrieNode() {
            children = new HashMap<>();
        }

        public void insert(String word) {
            if (word == null || word.isEmpty())
                return;
            char firstChar = word.charAt(0);
            TrieNode child = children.computeIfAbsent(firstChar, TrieNode::new);

            if (word.length() > 1) {
                child.insert(word.substring(1));
            } else {
                child.isWord = true;
                trieSize++;
            }
        }
    }

    TrieNode root;
    int trieSize;

//    public Trie(List<String> words) {
//        root = new TrieNode();
//        trieSize = 0;
//        for (String word : words)
//            root.insert(word);
//    }

    public Trie() {
        root = new TrieNode();
        trieSize = 0;
    }

    public void insert(String word) {
        root.insert(word);
    }

    public boolean find(String prefix, boolean exact) {
        TrieNode lastNode = root;
        for (char c : prefix.toCharArray()) {
            lastNode = lastNode.children.get(c);
            if (lastNode == null)
                return false;
        }
        return !exact || lastNode.isWord;
    }

    public void delete(String prefix, boolean exact) {
        TrieNode lastNode = root;
        if (this.find(prefix, exact)) {
            for (char c : prefix.toCharArray()) {
                lastNode = lastNode.children.get(c);
                if (lastNode.isWord) {
                    lastNode.isWord = false;
                    trieSize--;
                }
            }
        }
    }

    public boolean find(String prefix) {
        return find(prefix, false);
    }

    public int size() {
        return trieSize;
    }

    public void suggestHelper(TrieNode root, List<String> list, StringBuffer curr) {
        if (root.isWord) {
            list.add(curr.toString());
        }
        if (root.children == null || root.children.isEmpty())
            return;
        for (TrieNode child : root.children.values()) {
            suggestHelper(child, list, curr.append(child.c));
            curr.setLength(curr.length() - 1);
        }
    }

    public ArrayList<String> suggest(String prefix) {
        ArrayList<String> list = new ArrayList<>();
        TrieNode lastNode = root;
        StringBuffer curr = new StringBuffer();
        for (char c : prefix.toCharArray()) {
            lastNode = lastNode.children.get(c);
            if (lastNode == null)
                return list;
            curr.append(c);
        }
        suggestHelper(lastNode, list, curr);
        return list;
    }
}
