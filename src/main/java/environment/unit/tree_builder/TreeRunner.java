package environment.unit.tree_builder;

import environment.unit.tree_builder.nodes.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class TreeRunner
{
    private TreeBuilder builder;

    public TreeRunner(TreeBuilder builder)
    {
        this.builder = builder;
    }

    final public ArrayList<AbstractNode> findByClass(Class<?> clazz, AbstractNode from)
    {
        ArrayList<AbstractNode> list = new ArrayList<AbstractNode>();
        AbstractNode current = from == null ? this.builder.getRoot() : from;

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

        for (AbstractNode node : current.getChildren()) {
            list.addAll(this.findByClass(clazz, node));
        }

        return list;
    }

    final public ArrayList<AbstractNode> findByName(String name, AbstractNode from)
    {
        ArrayList<AbstractNode> list = new ArrayList<AbstractNode>();
        AbstractNode current = from == null ? this.builder.getRoot() : from;

        if (this.builder.getRoot() == null) {
            return list;
        }

        if (current.getChildren() == null) {
            return list;
        }

        if (current.getName() != null && current.getName().equals(name)) {
            list.add(current);
        }

        for (AbstractNode node : current.getChildren()) {
            list.addAll(this.findByName(name, node));
        }

        return list;
    }

    final public ArrayList<AbstractNode> getStack(AbstractNode from)
    {
        ArrayList<AbstractNode> list = new ArrayList<AbstractNode>();
        AbstractNode current = from == null ? this.builder.getRoot() : from;

        if (from != null) {
            list.add(from);
        }

        for (AbstractNode node : current.getChildren()) {
            list.addAll(this.getStack(node));
        }

        return list;
    }
}
