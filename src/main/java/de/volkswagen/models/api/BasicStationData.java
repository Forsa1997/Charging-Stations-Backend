package de.volkswagen.models.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class BasicStationData {

    private long id;
    private int operatorID;
    private int usageTypeID;
    private float[] latLng = new float[2];
    private String addressTitle;
    private String addressStreet;
    private String addressTown;
    private HashSet<Integer> connectionTypeId = new HashSet<>();
    private float maxPower=0;

    @Override
    public String toString() {
        return "BasicStationData{" +
                "id=" + id +
                ", operatorID=" + operatorID +
                ", usageTypeID=" + usageTypeID +
                ", latLng=" + Arrays.toString(latLng) +
                ", addressTitle='" + addressTitle + '\'' +
                ", addressStreet='" + addressStreet + '\'' +
                ", addressTown='" + addressTown + '\'' +
                ", connectionTypeId=" + connectionTypeId +
                ", maxPower=" + maxPower +
                '}';
    }

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

    public float[] getLatLng() {
        return latLng;
    }

    public void setLatLng(float[] latLng) {
        this.latLng = latLng;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressTown() {
        return addressTown;
    }

    public void setAddressTown(String addressTown) {
        this.addressTown = addressTown;
    }

    public HashSet<Integer> getConnectionTypeId() {
        return connectionTypeId;
    }

    public void setConnectionTypeId(HashSet<Integer> connectionTypeId) {
        this.connectionTypeId = connectionTypeId;
    }

    public float getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(float maxPower) {
        this.maxPower = maxPower;
    }

    public BasicStationData(StationData data) {
        this.id = data.getId();
        this.operatorID = data.getOperatorID();
        this.usageTypeID = data.getUsageTypeID();
        String lat = data.getAddressInfo().get("latitude");
        String lng = data.getAddressInfo().get("longitude");
        this.latLng[0] = Float.parseFloat(lat);
        this.latLng[1] = Float.parseFloat(lng);
        this.addressTitle = data.getAddressInfo().get("title");
        this.addressStreet = data.getAddressInfo().get("addressLine1");
        this.addressTown = data.getAddressInfo().get("town");
        data.getConnections().forEach(con -> {
            this.connectionTypeId.add(Integer.parseInt(con.get("connectionTypeID")));

            if (con.get("powerKW")!=null){
                if ( Float.parseFloat(con.get("powerKW"))>this.maxPower){
                    maxPower = Float.parseFloat(con.get("powerKW"));
                }
            }

        });
    }
}




