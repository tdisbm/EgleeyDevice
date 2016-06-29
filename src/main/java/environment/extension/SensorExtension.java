package environment.extension;

import environment.unit.Extension;
import environment.component.tree_builder.TreeBuilder;
import environment.component.tree_builder.nodes.*;
import sensor.Sensor;

import java.util.Map;

public class SensorExtension extends Extension
{
    @Override
    public void map(Object definition, Map.Entry prototype) throws Exception {
        if (!(definition instanceof Sensor)) {
            throw new Exception("Sensor class must abstract " + Sensor.class.getName());
        }
    }

    public String getPrefix() {
        return "@";
    }

    public String getPostfix() {
        return "";
    }

    public TreeBuilder buildPrototype(TreeBuilder treeBuilder) throws Exception {
        return treeBuilder.setRoot("sensors")
            .addChild(new InstanceNode("class"))
            .addChild(new DependencyNode("arguments"))
        .end();
    }
}
