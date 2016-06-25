package tasks;

import environment.extension.task.Task;
import io.socket.client.IO;
import io.socket.client.Socket;
import sensor.sensors.DHT22;

import java.net.URISyntaxException;

public class SymbiosisTask extends Task
{
    private Socket socket;
    private String host = "http://127.0.0.1:8000";
    private IO.Options opts = new IO.Options();

    public SymbiosisTask()
    {
        try {
            this.opts.query = "type=device&email=test";
            this.socket = IO.socket(this.host, this.opts);
            this.socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void run()
    {
        DHT22 dht22 = (DHT22) this.get("@DHT22");
        socket.emit("message", dht22.getCurrent());
    }
}
