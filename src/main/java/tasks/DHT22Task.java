package tasks;

import environment.extension.task.Task;
import sensor.sensors.DHT22;

public class DHT22Task extends Task
{
    public void run()
    {
        DHT22 dht22 = (DHT22) this.get("@DHT22");
        dht22.read();
    }
}
