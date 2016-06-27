package environment.extension;

import environment.unit.Extension;
import environment.component.tree_builder.TreeBuilder;
import environment.component.tree_builder.nodes.DependencyNode;
import environment.component.tree_builder.nodes.InstanceNode;
import environment.component.tree_builder.nodes.StringNode;
import tasks.Task;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskExtension extends Extension
{
    private static final long OUT_OF_MEMORY_DELAY = 20;

    private static final int  OUT_OF_MEMORY_ADDITIONAL_DELAY = 2000000;

    private int succedJobs = 0;

    @Override
    public void map(Object definition, Map.Entry prototype) throws Exception {
        if (!(definition instanceof Task)) {
            throw new Exception("tasks must be instance of Task");
        }

        LinkedHashMap config = (LinkedHashMap) prototype.getValue();
        Task task = (Task) definition;

        task.setRate((Integer) config.get("rate"));

        if (runSafeThreads(task)) {
            succedJobs++;
        }
    }

    public String getPrefix() {
        return "#";
    }

    public String getPostfix() {
        return "";
    }

    public TreeBuilder buildPrototype(TreeBuilder treeBuilder) throws Exception
    {
        return treeBuilder.setRoot("tasks")
            .addChild(new InstanceNode("class"))
            .addChild(new StringNode("rate"))
            .addChild(new DependencyNode("arguments"))
        .end();
    }

    private boolean runSafeThreads(Task task) {
        boolean success = false;
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        try {
            executor.scheduleAtFixedRate(task, 0, task.getRate(), TimeUnit.MILLISECONDS);
            success =  true;
        } catch (OutOfMemoryError e) {
            try {
                executor.wait(OUT_OF_MEMORY_DELAY, OUT_OF_MEMORY_ADDITIONAL_DELAY);
                runSafeThreads(task);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                success = false;
            }
        }

        return success;
    }
}
