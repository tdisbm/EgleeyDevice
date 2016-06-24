package environment.unit.tree_builder.nodes;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public abstract class AbstractNode implements NodeInterface
{
    private AbstractNode root;

    private String name;

    private Object value;

    private ArrayList<AbstractNode> children = new ArrayList<AbstractNode>();

    private ArrayList<AbstractNode> parents = new ArrayList<AbstractNode>();

    public AbstractNode(String name)
    {
        this.name = name;
    }

    final public AbstractNode setParent(AbstractNode parents)
    {
        this.parents.add(0, parents);

        return this;
    }

    final public AbstractNode addChild(AbstractNode child)
    {
        this.children.add(child);

        return this;
    }

    final public AbstractNode addParent(AbstractNode parent)
    {
        this.parents.add(parent);

        return this;
    }

    final public AbstractNode getChild(int i)
    {
        return this.children.get(i);
    }

    final public ArrayList<AbstractNode> getChildren()
    {
        return this.children;
    }

    final public AbstractNode getParent()
    {
        try {
            return this.parents.get(0);
        } catch (Exception ignored) {}

        return null;
    }

    final public AbstractNode getParent(int i)
    {
        return this.parents.get(i);
    }

    final public String getName()
    {
        return this.name;
    }

    final public Object getValue()
    {
        return value;
    }

    final public void setValue(Object value)
    {
        this.value = value;
    }

    final public void setRoot(AbstractNode root)
    {
        this.root = root;
    }

    final public AbstractNode getRoot()
    {
        return this.root;
    }
}
