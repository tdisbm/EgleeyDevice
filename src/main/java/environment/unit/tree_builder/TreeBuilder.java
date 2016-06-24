package environment.unit.tree_builder;


import environment.unit.tree_builder.nodes.AbstractNode;
import environment.unit.tree_builder.nodes.MapNode;
import environment.unit.tree_builder.nodes.StringNode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


public class TreeBuilder
{
    private AbstractNode root;

    private AbstractNode current;

    public TreeBuilder() {}

    public TreeBuilder(AbstractNode root)
    {
        this.root = root;
    }

    final public TreeBuilder setRoot(String name)
    {
        if (this.root != null) {
            return this;
        }

        this.root = new MapNode(name);
        this.current = this.root;

        return this;
    }

    @Contract("null -> fail")
    @NotNull
    final public TreeBuilder addChild(AbstractNode node) throws Exception
    {
        if (!(node instanceof StringNode)) {
            this.current.addChild(node);
            node.setRoot(this.root);
        }

        node.setParent(this.current);

        return this;
    }

    final public TreeBuilder end() throws Exception
    {
        AbstractNode next = this.current.getParent();

        if (next == null && this.current != this.root) {
            throw new Exception("Unexpected end of builder");
        }

        this.current = next == this.root ? this.root : next;

        return this;
    }

    final public TreeBuilder next(AbstractNode next)
    {
        next.setRoot(this.root);
        next.setParent(this.current);

        this.current.addChild(next);
        this.current = next;

        return this;
    }

    final public AbstractNode getRoot()
    {
        return this.root;
    }

    final public String getRootName()
    {
        return this.root.getName();
    }
}
