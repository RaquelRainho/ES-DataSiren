package com.springKafka.datasiren.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.springKafka.datasiren.model.Firefighter;
import com.springKafka.datasiren.model.Location;
import com.springKafka.datasiren.model.Notification;
import com.springKafka.datasiren.model.Sensor;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableScheduling
public class WebpageDataUpdate {

    @Autowired
    SimpMessagingTemplate template;

    private final HashMap<Integer, Firefighter> firefighters = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final ArrayNode outerArray = mapper.createArrayNode();
    private boolean first = true;

    @KafkaListener(topics = "esp24_GPS_v2", groupId = "UpdateWeb", containerFactory = "locationkafkaListenerContainerFactory")
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

    @KafkaListener(topics = "esp24_CO_v2", groupId = "UpdateWeb", containerFactory = "sensorkafkaListenerContainerFactory")
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

    @KafkaListener(topics = "esp24_heartRate_v2", groupId = "UpdateWeb", containerFactory = "sensorkafkaListenerContainerFactory")
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

    @KafkaListener(topics = "esp24_battery_v2", groupId = "UpdateWeb", containerFactory = "sensorkafkaListenerContainerFactory")
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

    @KafkaListener(topics = "esp24_temperature_v2", groupId = "UpdateWeb", containerFactory = "sensorkafkaListenerContainerFactory")
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

    @KafkaListener(topics = "esp24_humidity_v2", groupId = "UpdateWeb", containerFactory = "sensorkafkaListenerContainerFactory")
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

    @KafkaListener(topics = "esp24_notifications_v2", groupId = "UpdateWeb", containerFactory = "notificationkafkaListenerContainerFactory")
    public void NotificationWeb(@Payload Notification data) {

        ObjectNode outerObject1 = mapper.createObjectNode();
        outerObject1.put("id", data.getFirefighterID()).put("alert", data.getMessage());

        outerArray.add(outerObject1);
    }

    @Scheduled(fixedRate = 5000, initialDelay = 30000)
    private void UpdateWebPag() {
    	if(first) {
    		outerArray.removeAll();
    		first = false;
    	}
        JsonNode actualObj = mapper.valueToTree(firefighters.values().toArray());

        ObjectNode outerObject = mapper.createObjectNode();
        outerObject.putPOJO("Firefighters", actualObj);
        outerObject.putPOJO("alerts", outerArray);

        String newmessage = prettyPrintJsonString(outerObject);
        template.convertAndSend("/topic/esp24-data", newmessage);
        outerArray.removeAll();

        log.info(newmessage);
    }

    public String prettyPrintJsonString(JsonNode jsonNode) {
        try {
            Object json = mapper.readValue(jsonNode.toString(), Object.class
            );
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (Exception e) {
            return "Sorry, conversion error";
        }
    }
}
