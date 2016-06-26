package environment.extension.task;

import environment.unit.Container;


public abstract class Task implements Runnable
{
    private Container container;

    private int rate;

    final public Object get(String key)
    {
        return this.container.get(key);
    }

    final public Task setContainer(Container container)
    {
        this.container = container;

        return this;
    }

    final public int getRate()
    {
        return this.rate;
    }
}
