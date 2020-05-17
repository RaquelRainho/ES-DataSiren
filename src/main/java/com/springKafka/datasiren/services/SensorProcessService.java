package com.springKafka.datasiren.services;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    Gson g = new Gson();
    
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
        
        if(localizations.containsKey(id)){
            localizations.replace(id, localization);
        }else{
            localizations.put(id, localization);
        }
        //log.info(Arrays.toString(localization));

    }

    @KafkaListener(topics = "esp24_CO", groupId = "SensorProcessing", containerFactory = "SensorProcessingKafkaListenerContainerFactory")
    public void COProcess(@Payload String message) {
       
        String[] tmp = message.split(" ");
        
        int id = Integer.parseInt(tmp[0]);
        int value = Integer.parseInt(tmp[1]);
        
        String localization = getLocation(id);
        
        String tempmessage="";
        if (value > 800) {
            String text = "The firefighter " + id +
                                                        "is located in "+ localization + 
                                                        "and has entered a very dangerous environment.";
            tempmessage = g.toJson("{\"text\": " + text + "}");
            kafkaTemplate.send("esp24_notifications",       g.toJson(text));
            log.info(g.toJson(text));

        } else if (value > 250) {
            String text = "The firefighter " + id + "is located in "+ localization + "and has entered a dangerous environment.";
            //JSONObject jsonObject = new JSONObject();
            //try {
            //    jsonObject.put("text", text);
            //} catch (JSONException ex) {
            //    Logger.getLogger(SensorProcessService.class.getName()).log(Level.SEVERE, null, ex);
            //}
            //String payload = jsonObject.toString();
            kafkaTemplate.send("esp24_notifications",       g.toJson(text));
            log.info(g.toJson(text));
        } 
    }

    @KafkaListener(topics = "esp24_heartRate", groupId = "SensorProcessing", containerFactory = "SensorProcessingKafkaListenerContainerFactory")
    public void HeartRateProcess(@Payload String message) {
       
        String[] tmp = message.split(" ");

        int id = Integer.parseInt(tmp[0]);
        double value = Double.parseDouble(tmp[1]);

        String localization = getLocation(id);
        
        String tempmessage="";
        if (value < 60 | value > 150) {
            String text = "The firefighter " + id +
                                                        "is located in "+ localization + 
                                                        "and is probably injured or unconscious.";
            //JSONObject jsonObject = new JSONObject();
            //try {
            //    jsonObject.put("text", text);
            //} catch (JSONException ex) {
            //    Logger.getLogger(SensorProcessService.class.getName()).log(Level.SEVERE, null, ex);
            //}
            //String payload = jsonObject.toString();
            kafkaTemplate.send("esp24_notifications",       g.toJson(text));
            log.info(g.toJson(text));
        } 
    }
    
    @KafkaListener(topics = "esp24_battery", groupId = "SensorProcessing", containerFactory = "SensorProcessingKafkaListenerContainerFactory")
    public void BatteryProcess(@Payload String message) {

        String[] tmp = message.split(" ");

        int id = Integer.parseInt(tmp[0]);
        int value = Integer.parseInt(tmp[1]);

        String localization = getLocation(id);
        
        String tempmessage="";
        if (value <= 1) {
            String text = "Contact lost with the firefighter " + id +
                                                        ", whose last location received was " + localization + 
                                                        ", replacement battery needed.";
            //JSONObject jsonObject = new JSONObject();
            //try {
            //    jsonObject.put("text", text);
            //} catch (JSONException ex) {
            //    Logger.getLogger(SensorProcessService.class.getName()).log(Level.SEVERE, null, ex);
            //}
            //String payload = jsonObject.toString();
            kafkaTemplate.send("esp24_notifications",       g.toJson(text));
            log.info(g.toJson(text));
        }
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
