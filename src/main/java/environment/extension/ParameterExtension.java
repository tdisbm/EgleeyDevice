package environment.extension;


import environment.component.tree_builder.nodes.DependencyNode;
import environment.unit.Extension;
import environment.component.tree_builder.TreeBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public class ParameterExtension extends Extension
{
    @Override
    public void map(Object definition, Map.Entry prototype) {}

    public String getPrefix() {
        return "%";
    }

    public String getPostfix() {
        return "%";
    }

    public TreeBuilder buildPrototype(TreeBuilder treeBuilder) throws Exception {
        return new TreeBuilder(new DependencyNode("parameters"));
    }
}
