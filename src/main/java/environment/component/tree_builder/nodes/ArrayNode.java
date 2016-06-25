package environment.component.tree_builder.nodes;

import java.util.ArrayList;

public class ArrayNode extends Node
{
    public ArrayNode(String name) {
        super(name);
    }

    public boolean supports(Object value) {
        return value != null && value instanceof ArrayList;
    }

    public Object linearize(Object complex, Object linearized) {
        return complex;
    }

    public Object link(Object elements) {
        return null;
    }
}
