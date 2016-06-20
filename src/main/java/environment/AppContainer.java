package environment;

import environment.unit.container.AbstractContainer;
import environment.unit.container.ContainerInterface;

import java.util.LinkedHashMap;


public class AppContainer extends AbstractContainer
{
    private LinkedHashMap scenes;

    private LinkedHashMap tasks;

    private LinkedHashMap sensors;

    private LinkedHashMap parameters;

    public AppContainer setScenes(LinkedHashMap scenes)
    {
        this.scenes = scenes;

        return this;
    }

    public LinkedHashMap getScenes()
    {
        return scenes;
    }

    public AppContainer setSensors(LinkedHashMap sensors)
    {
        this.sensors = sensors;

        return this;
    }

    public LinkedHashMap getSensors()
    {
        return sensors;
    }

    public ContainerInterface setParameters(LinkedHashMap parameters) {
        this.parameters = parameters;

        return this;
    }

    public LinkedHashMap getParameters()
    {
        return parameters;
    }

    public AppContainer setTasks(LinkedHashMap tasks) {
        this.tasks = tasks;

        return this;
    }

    public LinkedHashMap getTasks() {
        return tasks;
    }
}
