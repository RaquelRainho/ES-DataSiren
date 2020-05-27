package com.springKafka.datasiren.services;

import com.springKafka.datasiren.model.Location;
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

    private final HashMap<Integer, Location> locations = new HashMap<>();
    private final HashMap<Integer, String> names = new HashMap<>();

    private String getMessage(int messageType, int id) {

        String message;

        switch (messageType) {
            case 0:
                message = "The firefighter id=" + id +" ( " + getName(id) + " )" 
                        + " is located in " + getLocation(id)
                        + " and has entered a dangerous environment.";
                break;
            case 1:
                message = "The firefighter id=" + id +" ( " + getName(id) + " )"
                        + " is located in " + getLocation(id)
                        + " and has entered a very dangerous environment.";
                break;
            case 2:
                message = "The firefighter id=" + id +" ( " + getName(id) + " )"
                        + " is located in " + getLocation(id)
                        + " and is probably injured or unconscious.";
                break;
            case 3:
                message = "Contact lost with the firefighter id=" + id +" ( " + getName(id) + " )"
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
        
        Location location = locations.get(id); 
        
        if (locations.containsKey(id)) {
            return "( Latitude: "  + location.getLatitude()+ " "
                    + "Longitude: " + location.getLongitude()+ " "
                    + "Elevation: " + location.getElevation()+ " )";
        } else {
            return "( unavelable )";
        }
    }

    private String getName(int id) {
        
        if (names.containsKey(id)) {
            return names.get(id);
        } else {
            return "unavelable";
        }
    }

    @KafkaListener(topics = "esp24_firefightersNames", groupId = "SensorProcessing", containerFactory = "senskafkaListenerContainerFactory")
    public void FirefightersNamesProcess(@Payload String message) {
       
        // Read name
        String [] tmp = message.split(" ");
        int id = Integer.parseInt(tmp[0]);
        String name = tmp[1];
        
        // Save name 
        if(names.containsKey(id) == false){
            names.put(id, name);
        }
    }
    
    @KafkaListener(topics = "esp24_GPS_v2", groupId = "SensorProcessing", containerFactory = "locationProcessingKafkaListenerContainerFactory")
    public void GPSProcess(@Payload Location data) {
        
        // Read GPS data
        int id = data.getFirefighterID();
        
        // Save GPS data 
        if (locations.containsKey(id)) {
            locations.replace(id, data);
        } else {
            locations.put(id, data);
        }
    }

    @KafkaListener(topics = "esp24_CO_v2", groupId = "SensorProcessing", containerFactory = "sensorProcessingKafkaListenerContainerFactory")
    public void COProcess(@Payload Sensor data) {

        // Read CO data
        int id = data.getFirefighterID();
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
        int id = data.getFirefighterID();
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
        int id = data.getFirefighterID();
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
