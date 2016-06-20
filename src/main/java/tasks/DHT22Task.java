package tasks;

import environment.unit.task.AbstractTask;
import sensor.sensors.DHT22;

public class DHT22Task extends AbstractTask
{
    public void run()
    {
        DHT22 dht22 = (DHT22) this.get("@DHT22");
        dht22.read();
    }
}
