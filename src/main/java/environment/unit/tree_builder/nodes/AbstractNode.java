package environment.unit.tree_builder.nodes;

import java.util.ArrayList;

public abstract class AbstractNode
{
    private AbstractNode parent;

    private String name;

    private Object value;

    private ArrayList<AbstractNode> children = new ArrayList<AbstractNode>();

    public AbstractNode(String name)
    {
        this.name = name;
    }

    public AbstractNode setParent(AbstractNode parent)
    {
        this.parent = parent;

        return this;
    }

    public AbstractNode addChild(AbstractNode child)
    {
        this.children.add(child);

        return this;
    }

    public AbstractNode getParent()
    {
        return this.parent;
    }

    public String getName()
    {
        return this.name;
    }
}
