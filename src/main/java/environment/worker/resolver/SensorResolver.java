package environment.worker.resolver;

import environment.unit.resolver.AbstractResolver;
import sensor.SensorInterface;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

public class SensorResolver extends AbstractResolver
{
    public Object map(Map.Entry entry) throws Exception
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

    public String getProperty()
    {
        return "sensors";
    }
}
