package environment.extension.scene;

import environment.unit.Extension;
import environment.component.tree_builder.TreeBuilder;
import environment.component.tree_builder.nodes.ArrayNode;
import environment.component.tree_builder.nodes.InstanceNode;
import environment.component.tree_builder.nodes.MapNode;
import environment.component.tree_builder.nodes.StringNode;
import scene.scenes.SceneInterface;

import java.util.Map;

public class SceneExtension extends Extension
{
    @Override
    public void map(Object definition, Map.Entry prototype) throws Exception {
        if (!(definition instanceof SceneInterface)) {
            throw new Exception("Scene class must implement " + SceneInterface.class.getName());
        }
    }

    /**
     * prefix to identify resolved options
     *
     * @return String
     */
    public String getPrefix() {
        return "@";
    }

    /**
     * Postfix to identify resolved options
     *
     * @return String
     */
    public String getPostfix() {
        return "";
    }

    public TreeBuilder buildPrototype(TreeBuilder treeBuilder) throws Exception {
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
}
