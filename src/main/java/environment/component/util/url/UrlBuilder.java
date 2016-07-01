package environment.component.util.url;


import java.util.*;

public class UrlBuilder {
    private Map<String, String> parameters = new HashMap<>();

    private String host;

    final public static int BUILD_QUERY = 1;

    final public static int BUILD_URL = 2;

    public UrlBuilder(String host, String port) {
        if (host == null) {
            return;
        }

        if (port != null) {
            host += ":" + port;
        }

        if (!host.substring(host.length() - 1, host.length()).equals("/")) {
            host += "/";
        }

        this.host = host;
    }

    final public UrlBuilder addParameter(String key, String value) {
        this.parameters.put(key, value);

        return this;
    }

    final public String getHost() {
        return this.host;
    }

    final public String buildUrl(int option) {
        String url = "";
        Collection<String> parameters = new ArrayList<>();

        switch (option) {
            case BUILD_QUERY:
                break;
            case BUILD_URL:
                url = this.host + "?";
                break;
            default:
                break;
        }

        for (Map.Entry<String, String> parameter: this.parameters.entrySet()) {
            parameters.add(parameter.getKey() + "=" + parameter.getValue());
        }

        return url + join(parameters, "&");
    }

    private static String join(Collection var0, String var1) {
        StringBuffer var2 = new StringBuffer();

        for(Iterator var3 = var0.iterator(); var3.hasNext(); var2.append((String)var3.next())) {
            if(var2.length() != 0) {
                var2.append(var1);
            }
        }

        return var2.toString();
    }
}
