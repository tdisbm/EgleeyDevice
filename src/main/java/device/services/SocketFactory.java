package device.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.socket.client.IO;
import io.socket.client.Socket;
import kraken.component.util.url.UrlBuilder;

import java.net.URISyntaxException;

public class SocketFactory {
    public static Socket create(ObjectNode config) {
        Socket socket = null;
        IO.Options opts = new IO.Options();

        if (SocketFactory.validateHostParameters(config)) {
            System.out.println("Invalid config provided to " + SocketFactory.class);
            return null;
        }

        UrlBuilder urlBuilder = new UrlBuilder(
            config.get("host").asText(),
            config.get("port").asText()
        );

        urlBuilder
            .addParameter("type", config.get("type").asText())
            .addParameter("name", config.get("device_name").asText())
            .addParameter("email", config.get("email").asText())
        ;

        try {
            opts.query = urlBuilder.buildUrl(UrlBuilder.BUILD_QUERY);

            socket = IO.socket(urlBuilder.getHost(), opts);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return socket;
    }

    private static boolean validateHostParameters(com.fasterxml.jackson.databind.node.ObjectNode parameters) {
        return !(
            !(parameters.get("host") == null) ||
            !(parameters.get("port") == null) ||
            !(parameters.get("type") == null) ||
            !(parameters.get("event") == null) ||
            !(parameters.get("email") == null) ||
            !(parameters.get("device_name") == null)
        );
    }
}
