package tasks;

import sensor.sensors.DHT22;

public class DHT22Task extends Task
{
    private DHT22 dht22;

    public DHT22Task(DHT22 dht22) {
        this.dht22 = dht22;
    }

    public void run() {
        System.out.println(dht22.getState());
        dht22.read();
    }
}
