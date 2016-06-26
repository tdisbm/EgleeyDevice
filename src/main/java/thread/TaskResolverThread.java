package thread;


import environment.extension.task.Task;
import environment.unit.Container;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskResolverThread implements Runnable
{
    private Container container;
    private static final long OUT_OF_MEMORY_DELAY = 20;
    private static final int  OUT_OF_MEMORY_ADDITIONAL_DELAY = 2000000;
    private int succedJobs = 0;

    public TaskResolverThread(Container container)
    {
        this.container = container;
    }

    public void run() {
        while (!this.container.isCompiled()) {}

        LinkedHashMap<?, Task> tasks = (LinkedHashMap) this.container.get("tasks");

        if (null == tasks) {
            return;
        }

        for (Map.Entry<?,Task> o : tasks.entrySet()) {
            Task task = o.getValue();
            task.setContainer(this.container);

            if(runSafeThreads(task)){
                succedJobs++;
            }
        }
    }

    private boolean runSafeThreads(Task task) {
        boolean succes = false;
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        try {
            executor.scheduleAtFixedRate(task, 0, task.getRate(), TimeUnit.MILLISECONDS);
            succes =  true;
        } catch (OutOfMemoryError e) {
            try {
                executor.wait(OUT_OF_MEMORY_DELAY, OUT_OF_MEMORY_ADDITIONAL_DELAY);
                runSafeThreads(task);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                succes = false;
            }
        }
        return succes;
    }
}
