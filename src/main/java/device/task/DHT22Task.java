package device.task;

import kraken.extension.task.Task;
import device.sensors.DHT22;

public class DHT22Task extends Task
{
    private DHT22 dht22;

    public DHT22Task(DHT22 dht22) {
        this.dht22 = dht22;
    }

    public void run() {
        dht22.read();
    }
}
