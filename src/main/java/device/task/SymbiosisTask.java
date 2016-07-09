package device.task;

import com.fasterxml.jackson.databind.node.ObjectNode;
import environment.extension.task.Task;
import io.socket.client.IO;
import io.socket.client.Socket;
import device.sensors.DHT22;
import environment.component.util.url.UrlBuilder;

import java.net.URISyntaxException;
import java.util.LinkedHashMap;

public class SymbiosisTask extends Task
{
    private Socket socket;
    private DHT22 dht22;
    private ObjectNode config;
    private LinkedHashMap<String, Object> state = new LinkedHashMap<>();

    public SymbiosisTask(DHT22 dht22, ObjectNode config) {
        if (this.validateHostParameters(config)) {
            System.out.println("Invalid config provided to " + this.getClass().getName());
            return;
        }

        this.config = config;

        UrlBuilder urlBuilder = new UrlBuilder(config.get("host").asText(), config.get("port").asText());
        urlBuilder.addParameter("type", config.get("type").asText())
            .addParameter("name", config.get("device_name").asText())
            .addParameter("email", config.get("email").asText());

        IO.Options opts = new IO.Options();
        opts.query = urlBuilder.buildUrl(UrlBuilder.BUILD_QUERY);

        try {
            this.socket = IO.socket(urlBuilder.getHost(), opts);
            this.socket.connect();
            this.dht22 = dht22;

            if (this.socket.connected()) {
                System.out.println("Symbiosis connected to " + urlBuilder.getHost());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private boolean validateHostParameters(ObjectNode parameters) {
        return !(
            !(parameters.get("host") == null) ||
            !(parameters.get("port") == null) ||
            !(parameters.get("type") == null) ||
            !(parameters.get("event") == null) ||
            !(parameters.get("email") == null) ||
            !(parameters.get("device_name") == null)
        );
    }

    public void run() {
        this.state.put("DHT22", dht22.getState());

        socket.emit(this.config.get("event").asText(), this.state);
    }
}
