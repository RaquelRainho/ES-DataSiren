package com.springKafka.datasiren.services;

import java.util.Arrays;
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

    HashMap <Integer,double[]> localizations = new HashMap <>();
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    
    private String getLocation(int id){
        
        try {
            double[] tmp1 = localizations.get(id);
            String localization = "";
            
            for (int k = 0; k < tmp1.length - 1; k++) {
                localization += String.valueOf(tmp1[k]) + " ";
            }            
            
            return localization;
        } catch (Exception e) {
        }
        return "unavelable";
    }
    
    @KafkaListener(topics = "esp24_GPS", groupId = "SensorProcessing", containerFactory = "SensorProcessingKafkaListenerContainerFactory")
    public void GPSProcess(@Payload String message) {
        
        String[] tmp = message.split(" ");

        int id = Integer.parseInt(tmp[0]);
        double[] localization = new double[tmp.length-1];

        for (int k=1; k < tmp.length-1; k++){
            localization[k-1]= Double.parseDouble(tmp[k]);
        }        
        
        localizations.put(id, localization);
        
        log.info(Arrays.toString(localization));

    }

    @KafkaListener(topics = "esp24_CO", groupId = "SensorProcessing", containerFactory = "SensorProcessingKafkaListenerContainerFactory")
    public void COProcess(@Payload String message) {
       
        String[] tmp = message.split(" ");
        
        int id = Integer.parseInt(tmp[0]);
        int value = Integer.parseInt(tmp[1]);
        
        String localization = getLocation(id);
        
        if (value > 800) {
            kafkaTemplate.send("esp24_notifications",   "The firefighter " + id +
                                                        "is located in "+ localization + 
                                                        "and has entered a very dangerous environment.");

        } else if (value > 250) {
            kafkaTemplate.send("esp24_notifications",   "The firefighter " + id +
                                                        "is located in "+ localization + 
                                                        "and has entered a dangerous environment.");
        } 
        log.info(localization + " id: " + id);
    }

        @KafkaListener(topics = "esp24_heartRate", groupId = "SensorProcessing", containerFactory = "SensorProcessingKafkaListenerContainerFactory")
    public void HeartRateProcess(@Payload String message) {
       
        String[] tmp = message.split(" ");

        int id = Integer.parseInt(tmp[0]);
        double value = Double.parseDouble(tmp[1]);

        String localization = getLocation(id);
        
     if (value < 60 | value > 150) {
        kafkaTemplate.send("esp24_notifications",       "The firefighter " + id +
                                                        "is located in "+ localization + 
                                                        "and is probably injured or unconscious.");
        } 
        log.info(localization + " id: " + id);
    }
    
    @KafkaListener(topics = "esp24_battery", groupId = "SensorProcessing", containerFactory = "SensorProcessingKafkaListenerContainerFactory")
    public void listen(@Payload String message) {

        String[] tmp = message.split(" ");

        int id = Integer.parseInt(tmp[0]);
        int value = Integer.parseInt(tmp[1]);

        String localization = getLocation(id);
        
        if (value <= 1) {
        kafkaTemplate.send("esp24_notifications",       "Contact lost with the firefighter " + id +
                                                        ", whose last location received was " + localization + 
                                                        ", replacement battery needed.");
        }
        log.info(localization + " id: " + id);
    }
    
    @KafkaListener(topics = "esp24_temperature", groupId = "SensorProcessing", containerFactory = "SensorProcessingKafkaListenerContainerFactory")
    public void TemperatureProcess(@Payload String message) {
    }

    @KafkaListener(topics = "esp24_HGT", groupId = "SensorProcessing", containerFactory = "SensorProcessingKafkaListenerContainerFactory")
    public void HGTProcess(@Payload String message) {
    }

    @KafkaListener(topics = "esp24_pressure", groupId = "SensorProcessing", containerFactory = "SensorProcessingKafkaListenerContainerFactory")
    public void PressureProcess(@Payload String message) {
    }

    @KafkaListener(topics = "esp24_NO2", groupId = "SensorProcessing", containerFactory = "SensorProcessingKafkaListenerContainerFactory")
    public void NO2Process(@Payload String message) {
    }

    @KafkaListener(topics = "esp24_humidity", groupId = "SensorProcessing", containerFactory = "SensorProcessingKafkaListenerContainerFactory")
    public void HumidityProcess(@Payload String message) {
    }

    @KafkaListener(topics = "esp24_luminosity", groupId = "SensorProcessing", containerFactory = "SensorProcessingKafkaListenerContainerFactory")
    public void LuminosityProcess(@Payload String message) {
    }
}
