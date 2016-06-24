package environment.worker.resolver;

import environment.unit.resolver.AbstractResolver;
import environment.unit.tree_builder.TreeBuilder;
import environment.unit.tree_builder.nodes.*;
import sensor.SensorInterface;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

public class SensorResolver extends AbstractResolver
{
    public Object resolve(Map.Entry entry) throws Exception
    {
        LinkedHashMap current = (LinkedHashMap) entry.getValue();
        LinkedHashMap gpio = (LinkedHashMap) current.get("gpio");

        Constructor<?> cons = Class
            .forName((String) current.get("class"))
            .getConstructor(int.class);

        Object obj;

        if (!((obj = cons.newInstance(gpio.get("pin"))) instanceof SensorInterface)) {
            throw new Exception("Sensor class must implement SensorInterface");
        }

        return obj;
    }

    public void done(LinkedHashMap instances) {}

    public String getPrefix()
    {
        return "@";
    }

    public String getPostfix()
    {
        return "";
    }

    public TreeBuilder buildConfigTree(TreeBuilder treeBuilder) throws Exception
    {
        return treeBuilder.setRoot("sensors")
            .addChild(new InstanceNode("class"))
            .addChild(new DependencyNode("arguments"))
            .next(new MapNode("gpio"))
                .addChild(new StringNode("pin"))
            .end()
        .end();
    }
}
