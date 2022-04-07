package de.volkswagen.models.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataStorage {

    private static List<BasicStationData> storedData = new ArrayList<>();
    private DataStorage (){}

    public static List<BasicStationData> getData(){
        return storedData;
    };

    public static void saveData(List<BasicStationData> data){
        storedData.clear();
        storedData.addAll(data);
    };

    public static void generateDataFromJSON () {
        List<BasicStationData> basicStationData = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File("src/main/resources/stationData.json");
            StationData[] stationData = mapper.readValue(file, StationData[].class);
            Arrays.stream(stationData).forEach(d -> basicStationData.add(new BasicStationData(d)));
            saveData(basicStationData);
            System.out.println("Done, imported "+ basicStationData.size() + " stations from local storage!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
