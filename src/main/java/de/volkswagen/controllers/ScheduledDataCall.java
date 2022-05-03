package de.volkswagen.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.volkswagen.models.api.BasicStationData;
import de.volkswagen.models.api.DataStorage;
import de.volkswagen.models.api.StationData;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class ScheduledDataCall {



    @Scheduled(fixedRate = 86400000)
    public void openChargeMapApiCall() {

        List<BasicStationData> basicStationData = new ArrayList<>();

        try {
            System.out.println("Trying to call the API");
            final HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "chargingStations");
            final HttpEntity<String> entity = new HttpEntity<String>(headers);
            final String uri = "https://api.openchargemap.io/v3/poi/?output=json&countrycode=DE&camelcase=true&maxresults=1000000&compact=true&verbose=false&key=326ba9d9-34a9-4031-9408-c17a289623b2";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<StationData[]> result = restTemplate.exchange(uri, HttpMethod.GET, entity, StationData[].class);
            StationData[] data = result.getBody();
            Arrays.stream(data).forEach(d -> basicStationData.add(new BasicStationData(d)));
            DataStorage.saveData(basicStationData);
            System.out.println("Done, imported "+ basicStationData.size() + " stations from the API!");
        } catch (Exception e) {
            DataStorage.generateDataFromJSON();
        }
    }
}
