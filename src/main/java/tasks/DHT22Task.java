package tasks;

import sensor.sensors.DHT22;

public class DHT22Task extends Task
{
    private DHT22 dht22;

    public DHT22Task(DHT22 dht22, String parameter) {
        this.dht22 = dht22;
    }

    public void run() {
        dht22.read();
    }
}
