package mecha;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class TrieNode<Value> implements Serializable {
    public TrieNode() {
        this.values = null;
        this.children = new HashMap<>();
    }

    public void setValues(ArrayList<Value> values) {
        this.values = values;
    }

    public ArrayList<Value> getValues() {
        return this.values;
    }

    public HashMap<Character, TrieNode<Value>> getChildren() {
        return this.children;
    }

    private ArrayList<Value> values;
    private HashMap<Character, TrieNode<Value>> children;
}
