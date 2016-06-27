package environment;

import environment.unit.Container;

import java.util.LinkedHashMap;


class AppContainer extends Container {
    private LinkedHashMap scenes;

    private LinkedHashMap tasks;

    private LinkedHashMap sensors;

    private LinkedHashMap parameters;

    public AppContainer setScenes(LinkedHashMap scenes) {
        this.scenes = scenes;

        return this;
    }

    public LinkedHashMap getScenes()
    {
        return scenes;
    }

    public Container setSensors(LinkedHashMap sensors) {
        this.sensors = sensors;

        return this;
    }

    public LinkedHashMap getSensors() {
        return sensors;
    }

    public Container setParameters(LinkedHashMap parameters) {
        this.parameters = parameters;

        return this;
    }

    public LinkedHashMap getParameters() {
        return parameters;
    }

    public Container setTasks(LinkedHashMap tasks) {
        this.tasks = tasks;

        return this;
    }

    public LinkedHashMap getTasks() {
        return tasks;
    }
}
