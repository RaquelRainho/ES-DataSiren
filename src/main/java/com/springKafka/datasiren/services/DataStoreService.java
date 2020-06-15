package com.springKafka.datasiren.services;

import com.springKafka.datasiren.model.Firefighter;
import com.springKafka.datasiren.model.FirefightersGroup;
import com.springKafka.datasiren.model.Location;
import com.springKafka.datasiren.model.Sensor;
import com.springKafka.datasiren.repository.FirefightersRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Service
@EnableScheduling
public class DataStoreService {

    @Autowired
    private FirefightersRepository firefightersRepository;

    private final HashMap<Integer, Firefighter> firefighters = new HashMap<>();
        
    @KafkaListener(topics = "esp24_GPS_v2", groupId = "mainDatabase", containerFactory = "mainDatabaseLocationsKafkaListenerContainerFactory")
    public void GPSWeb(@Payload Location data) {

        // Read CO data
        int firefighterId = data.getFirefighterID();
        double latitude = data.getLatitude();
        double longitude = data.getLongitude();
        double elevation = data.getElevation();

        // Update firefighter data
        Firefighter firefighter;

        if (firefighters.containsKey(firefighterId)) {
            firefighter = firefighters.get(firefighterId);
            firefighter.setLat(latitude);
            firefighter.setLongi(longitude);
            firefighter.setAlt(elevation);
            firefighters.replace(firefighterId, firefighter);
        } else {
            firefighter = new Firefighter();
            firefighter.setLat(latitude);
            firefighter.setLongi(longitude);
            firefighter.setAlt(elevation);
            firefighter.setId(firefighterId);
            firefighters.put(firefighterId, firefighter);
        }
    }

    @KafkaListener(topics = "esp24_CO_v2", groupId = "mainDatabase", containerFactory = "mainDatabaseSensorsKafkaListenerContainerFactory")
    public void COWeb(@Payload Sensor data) {

        // Read CO data
        int id = data.getFirefighterID();
        int value = (int) data.getValue();

        // Update firefighter data
        Firefighter firefighter;

        if (firefighters.containsKey(id)) {
            firefighter = firefighters.get(id);
            firefighter.setCO(value);
            firefighters.replace(id, firefighter);
        } else {
            firefighter = new Firefighter();
            firefighter.setCO(value);
            firefighter.setId(id);
            firefighters.put(id, firefighter);
        }
    }

    @KafkaListener(topics = "esp24_heartRate_v2", groupId = "mainDatabase", containerFactory = "mainDatabaseSensorsKafkaListenerContainerFactory")
    public void HeartRateWeb(@Payload Sensor data) {

        // Read heart rate data
        int id = data.getFirefighterID();
        double value = data.getValue();

        // Update firefighter data
        Firefighter firefighter;

        if (firefighters.containsKey(id)) {
            firefighter = firefighters.get(id);
            firefighter.setHr(value);
            firefighters.replace(id, firefighter);
        } else {
            firefighter = new Firefighter();
            firefighter.setHr(value);
            firefighter.setId(id);
            firefighters.put(id, firefighter);
        }
    }

    @KafkaListener(topics = "esp24_battery_v2", groupId = "mainDatabase", containerFactory = "mainDatabaseSensorsKafkaListenerContainerFactory")
    public void BatteryWeb(@Payload Sensor data) {

        // Read battery data
        int id = data.getFirefighterID();
        int value = (int) data.getValue();

        // Update firefighter data
        Firefighter firefighter;

        if (firefighters.containsKey(id)) {
            firefighter = firefighters.get(id);
            firefighter.setBat(value);
            firefighters.replace(id, firefighter);
        } else {
            firefighter = new Firefighter();
            firefighter.setBat(value);
            firefighter.setId(id);
            firefighters.put(id, firefighter);
        }
    }

    @KafkaListener(topics = "esp24_temperature_v2", groupId = "mainDatabase", containerFactory = "mainDatabaseSensorsKafkaListenerContainerFactory")
    public void TemperatureWeb(@Payload Sensor data) {

        // Read temperature data
        int id = data.getFirefighterID();
        int value = (int) data.getValue();

        // Update firefighter data
        Firefighter firefighter;

        if (firefighters.containsKey(id)) {
            firefighter = firefighters.get(id);
            firefighter.setTemp(value);
            firefighters.replace(id, firefighter);
        } else {
            firefighter = new Firefighter();
            firefighter.setTemp(value);
            firefighter.setId(id);

            firefighters.put(id, firefighter);
        }
    }

    @KafkaListener(topics = "esp24_humidity_v2", groupId = "mainDatabase", containerFactory = "mainDatabaseSensorsKafkaListenerContainerFactory")
    public void HumidityWeb(@Payload Sensor data) {

        // Read humidity data
        int id = data.getFirefighterID();
        int value = (int) data.getValue();

        // Update firefighter data
        Firefighter firefighter;

        if (firefighters.containsKey(id)) {
            firefighter = firefighters.get(id);
            firefighter.setHum(value);
            firefighters.replace(id, firefighter);
        } else {
            firefighter = new Firefighter();
            firefighter.setHum(value);
            firefighter.setId(id);

            firefighters.put(id, firefighter);
        }
    }

    @Scheduled(fixedRate = 5000, initialDelay = 30000)
    private void SaveOnDatabase() {

        FirefightersGroup p = new FirefightersGroup();
        List<Firefighter> tmp = new ArrayList<>();
        Firefighter tmp1;
        for (int i = 0; i < firefighters.size();i++){
            tmp1 = firefighters.get(i);
            tmp1.setId(0);
            tmp.add(tmp1);
        }

        p.setFirefighters(tmp);
        
        firefightersRepository.save(p);
        
        log.info(String.valueOf(firefightersRepository.findAll().size()));
    }
}
