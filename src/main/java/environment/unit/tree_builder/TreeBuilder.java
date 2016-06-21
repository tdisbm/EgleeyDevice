package environment.unit.tree_builder;


import environment.unit.tree_builder.nodes.AbstractNode;
import environment.unit.tree_builder.nodes.ArrayNode;

public class TreeBuilder
{
    private AbstractNode root;

    private AbstractNode current;

    public TreeBuilder setRoot(String name)
    {
        this.root = new ArrayNode(name);
        this.current = this.root;

        return this;
    }

    public TreeBuilder addChild(AbstractNode node)
    {
        node.setParent(this.current);
        this.current.addChild(node);

        return this;
    }

    public TreeBuilder end() throws Exception
    {
        AbstractNode next = this.current.getParent();

        if (next == null && this.current != this.root) {
            throw new Exception("Unexpected end of builder");
        }

        this.current = next == this.root ? this.root : next;

        return this;
    }

    public TreeBuilder next(AbstractNode next)
    {
        this.current.addChild(next);
        next.setParent(this.current);
        this.current = next;

        return this;
    }

    public String getRootName()
    {
        return this.root.getName();
    }
}
