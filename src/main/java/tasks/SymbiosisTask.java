package tasks;

import io.socket.client.IO;
import io.socket.client.Socket;
import sensor.sensors.DHT22;

import java.net.URISyntaxException;

public class SymbiosisTask extends Task
{
    private Socket socket;
    private String host = "http://127.0.0.1:8000";
    private IO.Options opts = new IO.Options();
    private DHT22 dht22;

    public SymbiosisTask(DHT22 dht22)
    {
        try {
            this.opts.query = "type=device&email=test";
            this.socket = IO.socket(this.host, this.opts);
            this.socket.connect();

            this.dht22 = dht22;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void run()
    {
        System.out.println(dht22.getCurrent().toString());
        socket.emit("message", dht22.getCurrent());
    }
}
