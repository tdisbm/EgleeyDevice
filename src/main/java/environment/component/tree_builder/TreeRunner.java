package environment.component.tree_builder;

import environment.component.tree_builder.nodes.*;

import java.util.ArrayList;

public class TreeRunner
{
    private TreeBuilder builder;

    public TreeRunner(TreeBuilder builder)
    {
        this.builder = builder;
    }

    final public ArrayList<Node> findByClass(Class<?> clazz, Node from)
    {
        ArrayList<Node> list = new ArrayList<Node>();
        Node current = from == null ? this.builder.getRoot() : from;

        if (this.builder.getRoot() == null) {
            return list;
        }

        if (current.getChildren() == null) {
            return list;
        }

        if (current.getClass().getName().equals(
            clazz.getName()
        )) {
            list.add(current);
        }

        for (Node node : current.getChildren()) {
            list.addAll(this.findByClass(clazz, node));
        }

        return list;
    }

    final public ArrayList<Node> findByName(String name, Node from)
    {
        ArrayList<Node> list = new ArrayList<Node>();
        Node current = from == null ? this.builder.getRoot() : from;

        if (this.builder.getRoot() == null) {
            return list;
        }

        if (current.getChildren() == null) {
            return list;
        }

        if (current.getName() != null && current.getName().equals(name)) {
            list.add(current);
        }

        for (Node node : current.getChildren()) {
            list.addAll(this.findByName(name, node));
        }

        return list;
    }

    final public ArrayList<Node> getStack(Node from)
    {
        ArrayList<Node> list = new ArrayList<Node>();
        Node current = from == null ? this.builder.getRoot() : from;

        if (from != null) {
            list.add(from);
        }

        for (Node node : current.getChildren()) {
            list.addAll(this.getStack(node));
        }

        return list;
    }
}
