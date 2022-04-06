package de.volkswagen.models.api;

import java.util.List;

public class DataStorage {

    private static List<BasicStationData> storedData;
    private DataStorage (){}

    public static List<BasicStationData> getData(){
        return storedData;
    };

    public static void saveData(List<BasicStationData> data){
        storedData = data;
    };
}
