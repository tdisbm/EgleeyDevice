package environment.extension;

import environment.unit.Extension;
import environment.component.tree_builder.TreeBuilder;
import environment.component.tree_builder.nodes.ArrayNode;
import environment.component.tree_builder.nodes.InstanceNode;
import environment.component.tree_builder.nodes.MapNode;
import environment.component.tree_builder.nodes.StringNode;
import scene.scenes.SceneInterface;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

public class SceneExtension extends Extension
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

    public TreeBuilder buildDefinitionPrototype(TreeBuilder treeBuilder) throws Exception
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
