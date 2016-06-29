package tasks;

import io.socket.client.IO;
import io.socket.client.Socket;
import sensor.sensors.DHT22;
import environment.component.util.url.UrlBuilder;

import java.net.URISyntaxException;
import java.util.LinkedHashMap;

public class SymbiosisTask extends Task
{
    private Socket socket;
    private DHT22 dht22;
    private LinkedHashMap<String, String> config;

    public SymbiosisTask(DHT22 dht22, LinkedHashMap<String, String> config)
    {
        if (this.validateHostParameters(config)) {
            System.out.println("Invalid config provided to " + this.getClass().getName());
            return;
        }

        this.config = config;

        UrlBuilder urlBuilder = new UrlBuilder(config.get("host"), config.get("port"));
        urlBuilder.addParameter("type", config.get("type"));

        IO.Options opts = new IO.Options();
        opts.query = urlBuilder.buildUrl(UrlBuilder.BUILD_QUERY);
        System.out.println(urlBuilder.buildUrl(UrlBuilder.BUILD_URL));
        try {
            this.socket = IO.socket(urlBuilder.getHost(), opts);
            this.socket.connect();
            this.dht22 = dht22;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private boolean validateHostParameters(LinkedHashMap parameters) {
        return !(
            !(parameters.get("host") == null) ||
            !(parameters.get("port") == null) ||
            !(parameters.get("type") == null) ||
            !(parameters.get("event") == null)
        );
    }

    public void run()
    {
        System.out.println(dht22.getState().toString());
        socket.emit(this.config.get("event"), dht22.getState());
    }
}
