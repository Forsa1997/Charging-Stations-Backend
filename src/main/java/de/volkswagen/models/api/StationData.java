package de.volkswagen.models.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StationData {

    private long id;
    private int operatorID;
    private int usageTypeID;
    private Map<String,String> addressInfo;
    private List<Map<String,String>> connections;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOperatorID() {
        return operatorID;
    }

    public void setOperatorID(int operatorID) {
        this.operatorID = operatorID;
    }

    public int getUsageTypeID() {
        return usageTypeID;
    }

    public void setUsageTypeID(int usageTypeID) {
        this.usageTypeID = usageTypeID;
    }

    public Map<String, String> getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(Map<String, String> addressInfo) {
        this.addressInfo = addressInfo;
    }

    public List<Map<String, String>> getConnections() {
        return connections;
    }

    public void setConnections(List<Map<String, String>> connections) {
        this.connections = connections;
    }

    @Override
    public String toString() {
        return "StationData{" +
                "id=" + id +
                ", operatorID=" + operatorID +
                ", usageTypeID=" + usageTypeID +
                ", addressInfo=" + addressInfo +
                ", connections=" + connections +
                '}';
    }
}


