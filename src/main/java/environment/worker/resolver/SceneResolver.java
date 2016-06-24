package environment.worker.resolver;

import environment.unit.resolver.AbstractResolver;
import environment.unit.tree_builder.TreeBuilder;
import environment.unit.tree_builder.nodes.ArrayNode;
import environment.unit.tree_builder.nodes.InstanceNode;
import environment.unit.tree_builder.nodes.MapNode;
import environment.unit.tree_builder.nodes.StringNode;
import scene.scenes.SceneInterface;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

public class SceneResolver extends AbstractResolver
{
    public Object resolve(Map.Entry entry) throws Exception
    {
        LinkedHashMap current = (LinkedHashMap) entry.getValue();
        Constructor<?> cons = Class
            .forName((String) current.get("class"))
            .getConstructor();

        Object obj;

        if (!((obj = cons.newInstance()) instanceof SceneInterface)) {
            throw new Exception("Scene class must implement SceneInterface");
        }

        return obj;
    }

    public void done(LinkedHashMap instances) {}

    /**
     * prefix to identify resolved options
     *
     * @return String
     */
    public String getPrefix()
    {
        return "@";
    }

    /**
     * Postfix to identify resolved options
     *
     * @return String
     */
    public String getPostfix()
    {
        return "";
    }

    public TreeBuilder buildConfigTree(TreeBuilder treeBuilder) throws Exception
    {
        return treeBuilder.setRoot("scenes")
            .addChild(new InstanceNode("class"))
            .addChild(new StringNode("template"))
            .next(new MapNode("options"))
                .addChild(new ArrayNode(null))
            .end()
            .next(new MapNode("config"))
                .addChild(new ArrayNode(null))
            .end()
        .end();
    }

    /**
     * @return String
     */
    public String getProperty()
    {
        return "scenes";
    }
}
