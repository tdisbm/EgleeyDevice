package device.task;

import com.fasterxml.jackson.databind.node.ObjectNode;
import device.sensors.DHT22;
import device.services.SocketFactory;
import io.socket.client.Socket;
import kraken.extension.task.Task;
import org.json.JSONArray;

public class SymbiosisTask extends Task
{
    private Socket socket;
    private DHT22 dht22;
    private ObjectNode config;
    private JSONArray state = new JSONArray();

    public SymbiosisTask(DHT22 dht22, ObjectNode config) {
        this.dht22 = dht22;
        this.config = config;
        this.connect(config);
    }

    private void connect(ObjectNode config) {
        socket = SocketFactory.create(config);
    }

    public void run() {
        this.state.put(dht22.getDevice().toJson());
        System.out.println(this.state);
        this.socket.emit(config.get("event").asText(), state);
        this.state = new JSONArray();
    }
}
