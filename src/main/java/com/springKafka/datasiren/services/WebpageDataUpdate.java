package com.springKafka.datasiren.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.springKafka.datasiren.model.Firefighter;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebpageDataUpdate {

    @Autowired
    SimpMessagingTemplate template;

    private HashMap <Integer,Firefighter> firefighters = new HashMap <>();    
    
    String[] names = {"Teresa", "Zé", "João"};
    
    @KafkaListener(topics = "esp24_GPS", groupId = "UpdateWeb", containerFactory = "UpdateWebKafkaListenerContainerFactory")
    public void GPSWeb(@Payload String message) {
        
        String[] tmp = message.split(" ");
        Firefighter firefighter;
        
        int id = Integer.parseInt(tmp[0]);
        double[] localization = new double[tmp.length-1];

        for (int k=1; k < tmp.length-1; k++){
            localization[k-1]= Double.parseDouble(tmp[k]);
        }        
        
        if(firefighters.containsKey(id)){
            firefighter=firefighters.get(id);
            firefighter.setLat(localization[0]);
            firefighter.setLongi(localization[1]);
            firefighter.setAlt(localization[2]);
            firefighters.replace(id, firefighter);
        }
        else{
            firefighter = new Firefighter();
            firefighter.setLat(localization[0]);
            firefighter.setLongi(localization[1]);
            firefighter.setAlt(localization[2]);        
            firefighters.put(id, firefighter);
        }
        
        ObjectMapper mapper = new ObjectMapper();    
        JsonNode actualObj = mapper.valueToTree(firefighters.values().toArray());
        String newmessage = prettyPrintJsonString(actualObj);
        template.convertAndSend("/topic/esp24-data", newmessage);
        
        //log.info(firefighter.toString());
        //log.info(newmessage);
        
    }
    
    @KafkaListener(topics = "esp24_CO", groupId = "UpdateWeb", containerFactory = "UpdateWebKafkaListenerContainerFactory")
    public void COWeb(@Payload String message) {
        
        String[] tmp = message.split(" ");
        Firefighter firefighter;
        
        int id = Integer.parseInt(tmp[0]);
        int value = Integer.parseInt(tmp[1]);
        
        if(firefighters.containsKey(id)){
            firefighter=firefighters.get(id);
            firefighter.setCO(value);
            firefighters.replace(id, firefighter);
        }
        else{
            firefighter = new Firefighter();
            firefighter.setCO(value);
            firefighters.put(id, firefighter);
        }
        
        ObjectMapper mapper = new ObjectMapper();    
        JsonNode actualObj = mapper.valueToTree(firefighters.values().toArray());
        String newmessage = prettyPrintJsonString(actualObj);
        template.convertAndSend("/topic/esp24-data", newmessage);
        
    }
    
    @KafkaListener(topics = "esp24_heartRate", groupId = "UpdateWeb", containerFactory = "UpdateWebKafkaListenerContainerFactory")
    public void HeartRateWeb(@Payload String message) {
        
        String[] tmp = message.split(" ");
        Firefighter firefighter;
        
        int id = Integer.parseInt(tmp[0]);
        double value = Double.parseDouble(tmp[1]);
        
        if(firefighters.containsKey(id)){
            firefighter=firefighters.get(id);
            firefighter.setHr(value);
            firefighters.replace(id, firefighter);
        }
        else{
            firefighter = new Firefighter();
            firefighter.setHr(value);
            firefighters.put(id, firefighter);
        }
        
        ObjectMapper mapper = new ObjectMapper();    
        JsonNode actualObj = mapper.valueToTree(firefighters.values().toArray());
        String newmessage = prettyPrintJsonString(actualObj);
        template.convertAndSend("/topic/esp24-data", newmessage);
    }
    
    @KafkaListener(topics = "esp24_battery", groupId = "UpdateWeb", containerFactory = "UpdateWebKafkaListenerContainerFactory")
    public void BatteryWeb(@Payload String message) {
        
        String[] tmp = message.split(" ");
        Firefighter firefighter;
        
        int id = Integer.parseInt(tmp[0]);
        int value = Integer.parseInt(tmp[1]);
        
        if(firefighters.containsKey(id)){
            firefighter=firefighters.get(id);
            firefighter.setBat(value);
            firefighters.replace(id, firefighter);
        }
        else{
            firefighter = new Firefighter();
            firefighter.setBat(value);
            firefighters.put(id, firefighter);
        }
        
        ObjectMapper mapper = new ObjectMapper();    
        JsonNode actualObj = mapper.valueToTree(firefighters.values().toArray());
        String newmessage = prettyPrintJsonString(actualObj);
        template.convertAndSend("/topic/esp24-data", newmessage);
        
    }
    
    @KafkaListener(topics = "esp24_temperature", groupId = "UpdateWeb", containerFactory = "UpdateWebKafkaListenerContainerFactory")
    public void TemperatureWeb(@Payload String message) {
        
        String[] tmp = message.split(" ");
        Firefighter firefighter;
        
        int id = Integer.parseInt(tmp[0]);
        int value = Integer.parseInt(tmp[1]);
        
        if(firefighters.containsKey(id)){
            firefighter=firefighters.get(id);
            firefighter.setTemp(value);
            firefighters.replace(id, firefighter);
        }
        else{
            firefighter = new Firefighter();
            firefighter.setTemp(value);
            firefighters.put(id, firefighter);
        }
        
        ObjectMapper mapper = new ObjectMapper();    
        JsonNode actualObj = mapper.valueToTree(firefighters.values().toArray());
        String newmessage = prettyPrintJsonString(actualObj);
        template.convertAndSend("/topic/esp24-data", newmessage);
        
    }
    
    @KafkaListener(topics = "esp24_humidity", groupId = "UpdateWeb", containerFactory = "UpdateWebKafkaListenerContainerFactory")
    public void HumidityWeb(@Payload String message) {

        String[] tmp = message.split(" ");
        Firefighter firefighter;
        
        int id = Integer.parseInt(tmp[0]);
        int value = Integer.parseInt(tmp[1]);
        
        if(firefighters.containsKey(id)){
            firefighter=firefighters.get(id);
            firefighter.setHum(value);
            firefighters.replace(id, firefighter);
        }
        else{
            firefighter = new Firefighter();
            firefighter.setHum(value);
            firefighters.put(id, firefighter);
        }
        
        ObjectMapper mapper = new ObjectMapper();    
        JsonNode actualObj = mapper.valueToTree(firefighters.values().toArray());
        String newmessage = prettyPrintJsonString(actualObj);
        template.convertAndSend("/topic/esp24-data", newmessage);
    }
    
    @KafkaListener(topics = "esp24_notifications", groupId = "UpdateWeb", containerFactory = "UpdateWebKafkaListenerContainerFactory")
    public void NotificationWeb(@Payload String message) {
        
        String tmp = message.split(" ")[0];
        tmp = tmp.substring(1, tmp.length());

        int id = Integer.parseInt(tmp);
        String messag = message.substring(tmp.length()+2,message.length()-1);

        
        ObjectMapper mapper = new ObjectMapper();    
        JsonNode actualObj = mapper.valueToTree(firefighters.values().toArray());
       
        /*
        ObjectNode jsonn = mapper.valueToTree(actualObj);
        ArrayNode alertsArray = mapper.createArrayNode();
        ObjectNode alert = mapper.createObjectNode();
        alert.put("id", "" + id);
        alert.put("alert", message);
        alertsArray.add(alert);
        jsonn.putArray("alerts").addAll(alertsArray);
        */
        
        String newmessage = prettyPrintJsonString(actualObj);
        template.convertAndSend("/topic/esp24-data", newmessage);
        
        log.info(message);
        log.info(String.valueOf(id));
        log.info(messag);

    }

    public String prettyPrintJsonString(JsonNode jsonNode) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(jsonNode.toString(), Object.class
            );
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (Exception e) {
            return "Sorry, conversion error";
        }
    }

    public boolean isNumeric(String str) {
        try {
            @SuppressWarnings("unused")
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
