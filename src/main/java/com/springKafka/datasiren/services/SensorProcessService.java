package com.springKafka.datasiren.services;

import com.springKafka.datasiren.model.Notification;
import com.springKafka.datasiren.model.Sensor;
import java.util.HashMap;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SensorProcessService {

    @Autowired
    private KafkaTemplate<String, Notification> notificationKafkaTemplate;

    private final HashMap<Integer, double[]> locations = new HashMap<>();
    private final HashMap<Integer, String> names = new HashMap<>();

    private String getMessage(int messageType, int id) {

        String message;

        switch (messageType) {
            case 0:
                message = "The firefighter " + id + " is located in " + getLocation(id)
                        + " and has entered a dangerous environment.";
                break;
            case 1:
                message = "The firefighter " + id + " is located in " + getLocation(id)
                        + " and has entered a very dangerous environment.";
                break;
            case 2:
                message = "The firefighter " + id + " is located in " + getLocation(id)
                        + " and is probably injured or unconscious.";
                break;
            case 3:
                message = "Contact lost with the firefighter " + id
                        + ", whose last location received was " + getLocation(id)
                        + ", replacement battery needed.";
                break;
            default:
                message = "ERROR";
                break;

        }
        return message;
    }

    private String getLocation(int id) {
        if (locations.containsKey(id)) {
            double[] tmp1 = locations.get(id);
            String location = "";

            for (int k = 0; k < tmp1.length - 1; k++) {
                location += String.valueOf(tmp1[k]) + " ";
            }
            
            return location;
        } else {
            return "unavelable";
        }
    }

    private String getName(int id) {

        if (names.containsKey(id)) {
            return names.get(id);
        } else {
            return "unavelable";
        }
    }

    @KafkaListener(topics = "esp24_GPS", groupId = "SensorProcessing", containerFactory = "SensorProcessingKafkaListenerContainerFactory")
    public void GPSProcess(@Payload String message) {

        // Read GPS data
        String[] tmp = message.split(" ");
        int id = Integer.parseInt(tmp[0]);
        double[] location = new double[tmp.length - 1];

        for (int k = 1; k < tmp.length - 1; k++) {
            location[k - 1] = Double.parseDouble(tmp[k]);
        }

        // Save GPS data 
        if (locations.containsKey(id)) {
            locations.replace(id, location);
        } else {
            locations.put(id, location);
        }
    }

    @KafkaListener(topics = "esp24_CO_v2", groupId = "SensorProcessing", containerFactory = "sensorProcessingKafkaListenerContainerFactory")
    public void COProcess(@Payload Sensor data) {

        // Read CO data
        int id = data.getId();
        double value = data.getValue();
        String time = data.getTime();
        String name = getName(id);

        // Create and send Notification 
        int MESSAGE_TYPE = 0;
        Notification notification;

        if (value > 250) {
            if (value > 800) {
                MESSAGE_TYPE = 1;
            }
            notification = new Notification(id, name, time, getMessage(MESSAGE_TYPE, id));
            notificationKafkaTemplate.send("esp24_notifications_v2", notification);
        }
    }

    @KafkaListener(topics = "esp24_heartRate_v2", groupId = "SensorProcessing", containerFactory = "sensorProcessingKafkaListenerContainerFactory")
    public void HeartRateProcess(@Payload Sensor data) {

        // Read heart rate data
        int id = data.getId();
        double value = data.getValue();
        String time = data.getTime();
        String name = getName(id);

        // Create and send Notification 
        int MESSAGE_TYPE;
        Notification notification;

        if (value < 60 | value > 150) {
            MESSAGE_TYPE = 2;
            notification = new Notification(id, name, time, getMessage(MESSAGE_TYPE, id));
            notificationKafkaTemplate.send("esp24_notifications_v2", notification);
        }
    }

    @KafkaListener(topics = "esp24_battery_v2", groupId = "SensorProcessing", containerFactory = "sensorProcessingKafkaListenerContainerFactory")
    public void BatteryProcess(@Payload Sensor data) {

        // Read battery data
        int id = data.getId();
        double value = data.getValue();
        String time = data.getTime();
        String name = getName(id);

        // Create and send Notification 
        int MESSAGE_TYPE;
        Notification notification;

        if (value <= 1) {
            MESSAGE_TYPE = 3;
            notification = new Notification(id, name, time, getMessage(MESSAGE_TYPE, id));
            notificationKafkaTemplate.send("esp24_notifications_v2", notification);
        }
    }

    /*
    
    @KafkaListener(topics = "esp24_temperature_v2", groupId = "SensorProcessing", containerFactory = "sensorProcessingKafkaListenerContainerFactory")
    public void TemperatureProcess(@Payload Sensor data) {
    }

    @KafkaListener(topics = "esp24_humidity_v2", groupId = "SensorProcessing", containerFactory = "sensorProcessingKafkaListenerContainerFactory")
    public void HumidityProcess(@Payload Sensor data) {
    }

     */
}
