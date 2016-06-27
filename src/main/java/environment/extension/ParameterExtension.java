package environment.extension;


import environment.unit.Extension;
import environment.component.tree_builder.TreeBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public class ParameterExtension extends Extension
{
    public Object resolve(Map.Entry entry) throws Exception
    {
        return entry.getValue();
    }

    public void done(LinkedHashMap instances)
    {

    }

    public String getPrefix() {
        return "%";
    }

    public String getPostfix() {
        return "%";
    }

    public TreeBuilder buildPrototype(TreeBuilder treeBuilder) throws Exception
    {
        return treeBuilder.setRoot("parameters");
    }
}
