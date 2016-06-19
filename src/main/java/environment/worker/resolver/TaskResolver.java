package environment.worker.resolver;

import environment.unit.resolver.AbstractResolver;
import environment.unit.task.AbstractTask;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;

public class TaskResolver extends AbstractResolver
{
    public Object map(Map.Entry entry) throws Exception
    {
        LinkedHashMap config = (LinkedHashMap) entry.getValue();
        String className = (String) config.get("class");
        Integer rate = (Integer) config.get("rate");

        Constructor<?> cons = Class
            .forName(className)
            .getConstructor();

        AbstractTask task = (AbstractTask) cons.newInstance();
        task.setContainer(this.getContainer());

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(task, 0, rate, TimeUnit.MILLISECONDS);

        return executor;
    }

    public String getPrefix() {
        return "#";
    }

    public String getProperty() {
        return "tasks";
    }
}
