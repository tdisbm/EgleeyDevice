package environment.unit.task;

import environment.unit.container.ContainerInterface;


public abstract class AbstractTask implements Runnable
{
    private ContainerInterface container;

    private int rate;

    final public Object get(String key)
    {
        return this.container.get(key);
    }

    final public AbstractTask setContainer(ContainerInterface container)
    {
        this.container = container;

        return this;
    }

    final public int getRate()
    {
        return this.rate;
    }
}
