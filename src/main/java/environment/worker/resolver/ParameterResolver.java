package environment.worker.resolver;


import environment.unit.resolver.AbstractResolver;
import environment.unit.tree_builder.TreeBuilder;
import environment.unit.tree_builder.nodes.ArrayNode;
import environment.unit.tree_builder.nodes.StringNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class ParameterResolver extends AbstractResolver
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

    public TreeBuilder buildConfigTree(TreeBuilder treeBuilder) throws Exception
    {
        return treeBuilder.setRoot("parameters")
            .addChild(new StringNode(null))
        .end();
    }

    public String getProperty() {
        return "parameters";
    }
}
