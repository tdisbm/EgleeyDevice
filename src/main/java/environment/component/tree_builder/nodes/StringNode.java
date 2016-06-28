package environment.component.tree_builder.nodes;

public class StringNode extends Node
{
    public StringNode(String name)
    {
        super(name);
    }

    public boolean supports(Object value) {
        return value != null && value instanceof String;
    }

    public Object linearize(Object complex, Object linearized) {
        return complex;
    }
}
