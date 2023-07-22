package mecha;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

public class Trie<Value> implements Serializable {
    public Trie() {
        root = new TrieNode<>();
    }

    public void add(String word, Value value) {
        TrieNode<Value> node = root;
        for (Character ch : word.toCharArray()) {
            if (!node.getChildren().containsKey(ch))
                node.getChildren().put(ch, new TrieNode<>());
            node = node.getChildren().get(ch);
        }
        if (node.getValues() == null) {
            ArrayList<Value> values = new ArrayList<>();
            values.add(value);
            node.setValues(values);
        } else
            node.getValues().add(value);
    }

    public ArrayList<Value> search_all(String prefix) {
        ArrayList<Value> res = new ArrayList<>();
        TrieNode<Value> node = this.root;
        for (Character ch : prefix.toCharArray()) {
            if (!node.getChildren().containsKey(ch))
                return res;
            node = node.getChildren().get(ch);
        }
        Stack<TrieNode<Value>> stack = new Stack<>();
        stack.push(node);
        while (!stack.isEmpty()) {
            node = stack.pop();
            if (node.getValues() != null)
                res.addAll(node.getValues());
            for (Character ch : node.getChildren().keySet())
                stack.push(node.getChildren().get(ch));
        }
        return res;
    }

    private final TrieNode<Value> root;
}
