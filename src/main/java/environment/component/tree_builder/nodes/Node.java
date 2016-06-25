package environment.component.tree_builder.nodes;

import java.util.ArrayList;

public abstract class Node
{
    private Node root;

    private String name;

    private Object value;

    private int level;

    private ArrayList<Node> children = new ArrayList<Node>();

    private ArrayList<Node> parents = new ArrayList<Node>();

    public Node(String name) {
        this.name = name;
    }

    final public Node setParent(Node parents) {
        this.parents.add(0, parents);

        return this;
    }

    final public Node addChild(Node child) {
        this.children.add(child);

        return this;
    }

    final public Node addParent(Node parent) {
        this.parents.add(parent);

        return this;
    }

    final public Node getChild(int i) {
        return this.children.get(i);
    }

    final public ArrayList<Node> getChildren() {
        return this.children;
    }

    final public Node getParent() {
        try {
            return this.parents.get(0);
        } catch (Exception ignored) {}

        return null;
    }

    final public Node getParent(int i) {
        return this.parents.get(i);
    }

    final public String getName() {
        return this.name;
    }

    final public Object getValue() {
        return value;
    }

    final public Node setValue(Object value) {
        this.value = value;

        return this;
    }

    final public Node setRoot(Node root) {
        this.root = root;

        return this;
    }

    final public Node getRoot() {
        return this.root;
    }

    public int getLevel() {
        return level;
    }

    public Node setLevel(int level) {
        this.level = level;

        return this;
    }

    public abstract boolean supports(Object value);

    public abstract Object linearize(Object complex, Object linearized);
}
