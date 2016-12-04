package device.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class Device {
    private String name;

    private Map<String, Object> data;

    private ArrayList<Device> devices;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Device setData(Map<String, Object> data) {
        this.data = data;

        return this;
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public Device setDevices(ArrayList<Device> devices) {
        this.devices = devices;

        return this;
    }

    public Device addDevice(Device device) {
        this.devices.add(device);

        return this;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        JSONArray list = null;

        try {
            obj.put("name", name);
            obj.put("data", data);

            if (devices != null) {
                list = new JSONArray();
                for (Device device : devices) {
                    list.put(device.toJson());
                }
            }

            obj.put("devices", list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
