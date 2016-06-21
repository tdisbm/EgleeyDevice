package environment.unit.dependency_injection;

import environment.unit.tree_builder.nodes.AbstractNode;

public class Dependency extends AbstractNode
{
    public String className = "";

    public Dependency(String name)
    {
        super(name);
    }
}
