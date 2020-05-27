package com.springKafka.datasiren.services;

import com.google.gson.Gson;
import com.springKafka.datasiren.model.Firefighter;
import com.springKafka.datasiren.model.FirefightersGroup;
import com.springKafka.datasiren.model.Sensor;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Service
public class KafkaConsumerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, Sensor> sensorKafkaTemplate;

    @KafkaListener(topics = "${kafka.topic}", groupId = "esp24_AllSensorData", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload String message) {

        String actualTime = LocalDateTime.now().toString();
        
        Gson g = new Gson();
        FirefightersGroup p = g.fromJson(message, FirefightersGroup.class);
        int id = 0;
        
        for (Firefighter f : p.getFirefighters()) {
            kafkaTemplate.send("esp24_GPS", id + " " + f.getLat() + " " + f.getLongi() + " " + f.getAlt());

            sensorKafkaTemplate.send("esp24_CO_v2"         , new Sensor(id, actualTime, (double)f.getCO()));
            sensorKafkaTemplate.send("esp24_heartRate_v2"  , new Sensor(id, actualTime, (double)f.getHr()));
            sensorKafkaTemplate.send("esp24_battery_v2"    , new Sensor(id, actualTime, (double)f.getBat()));
            sensorKafkaTemplate.send("esp24_temperature_v2", new Sensor(id, actualTime, (double)f.getTemp()));
            sensorKafkaTemplate.send("esp24_humidity_v2"   , new Sensor(id, actualTime, (double)f.getHum()));

            id++;

        }
    }
}
