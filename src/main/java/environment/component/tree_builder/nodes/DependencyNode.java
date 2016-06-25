package environment.component.tree_builder.nodes;

import java.util.ArrayList;

public class DependencyNode extends Node
{
    public DependencyNode(String name)
    {
        super(name);
    }

    public boolean supports(Object value) {
        return value != null && value instanceof ArrayList;
    }

    public Object linearize(Object complex, Object linearized) {
        ArrayList linearizedMirror = linearized == null ? new ArrayList() : (ArrayList) linearized;
        ArrayList complexMirror = (ArrayList) complex;

        for (Object o : complexMirror) {
            if (o instanceof ArrayList) {
                linearizedMirror = (ArrayList) this.linearize(o, linearizedMirror);
            } else {
                linearizedMirror.add(o);
            }
        }

        return linearizedMirror;
    }

    public Object link(Object elements)
    {
        return elements;
    }
}
