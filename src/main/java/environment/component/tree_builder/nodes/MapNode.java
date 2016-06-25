package environment.component.tree_builder.nodes;

import java.util.LinkedHashMap;

public class MapNode extends Node
{
    public MapNode(String name) {
        super(name);
    }

    public boolean supports(Object value) {
        return value != null && value instanceof LinkedHashMap;
    }

    public Object linearize(Object complex, Object linearized) {
        return complex;
    }

    public Object link(Object elements) {
        return null;
    }
}
