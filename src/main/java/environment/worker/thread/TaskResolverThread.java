package environment.worker.thread;


import environment.unit.container.ContainerInterface;
import environment.unit.task.AbstractTask;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskResolverThread implements Runnable
{
    private ContainerInterface container;

    public TaskResolverThread(ContainerInterface container)
    {
        this.container = container;
    }

    public void run() {
        while (!this.container.isCompiled()) {}

        LinkedHashMap tasks = (LinkedHashMap) this.container.get("tasks");

        if (null == tasks) {
            return;
        }

        for (Object o : tasks.entrySet()) {
            AbstractTask task = (AbstractTask) ((Map.Entry) o).getValue();
            task.setContainer(this.container);

            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(task, 0, task.getRate(), TimeUnit.MILLISECONDS);
        }
    }
}
