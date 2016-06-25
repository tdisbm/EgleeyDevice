package environment.component.tree_builder.nodes;

public class InstanceNode extends Node
{
    public InstanceNode(String name) {
        super(name);
    }

    public boolean supports(Object value) {
        if (value == null || !(value instanceof String)) {
            return false;
        }

        try {
            Class.forName((String) value);
        } catch( ClassNotFoundException e ) {
            return false;
        } catch (ClassCastException e) {
            return false;
        }

        return true;
    }

    public Object linearize(Object complex, Object linearized) {
        return complex;
    }

    public Object link(Object elements) {
        return null;
    }
}
