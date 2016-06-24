package environment.unit.tree_builder.nodes;

public class DependencyNode extends AbstractNode
{
    public DependencyNode(String name)
    {
        super(name);
    }

    public Object link(Object elements)
    {
        return elements;
    }
}
