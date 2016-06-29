package sensor;

import java.util.HashMap;
import java.util.Map;

public abstract class Sensor {
    private Map<String, Object> state = new HashMap<>();

    final public Sensor write(String key, Object value) {
        this.state.put(key, value);

        return this;
    }

    final public Map<String, Object> getState() {
        return this.state;
    }

    final public String toString() {
        return this.state.toString();
    }

    public abstract void read();
}
