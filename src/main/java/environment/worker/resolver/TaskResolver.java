package environment.worker.resolver;

import environment.unit.resolver.AbstractResolver;
import environment.unit.task.AbstractTask;
import environment.unit.tree_builder.TreeBuilder;
import environment.unit.tree_builder.nodes.ArrayNode;
import environment.unit.tree_builder.nodes.InstanceNode;
import environment.unit.tree_builder.nodes.StringNode;
import environment.worker.thread.TaskResolverThread;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class TaskResolver extends AbstractResolver
{
    public Object resolve(Map.Entry entry) throws Exception
    {
        LinkedHashMap config = (LinkedHashMap) entry.getValue();
        String className = (String) config.get("class");

        Constructor<?> cons = Class
            .forName(className)
            .getConstructor();

        AbstractTask task = (AbstractTask) cons.newInstance();
        //task.setContainer(this.getContainer());

        Field rate = task
            .getClass()
            .getSuperclass()
            .getDeclaredField("rate");

        rate.setAccessible(true);
        rate.set(task, config.get("rate"));
        rate.setAccessible(false);

        return task;
    }

    public void done(LinkedHashMap instances)
    {
        //TaskResolverThread resolverThread = new TaskResolverThread(this.getContainer());
        //Thread master = new Thread(resolverThread);
        //master.start();
    }

    public String getPrefix()
    {
        return "#";
    }

    public String getPostfix()
    {
        return "";
    }

    public TreeBuilder buildConfigTree(TreeBuilder treeBuilder) throws Exception
    {
        return treeBuilder.setRoot("tasks")
            .addChild(new InstanceNode("class"))
            .addChild(new StringNode("rate"))
        .end();
    }
}
