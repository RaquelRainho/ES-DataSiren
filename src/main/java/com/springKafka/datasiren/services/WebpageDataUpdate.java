package com.springKafka.datasiren.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public void GPSProcess(@Payload String message) {
        
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
        JsonNode actualObj = mapper.valueToTree(firefighters);
        String newmessage = prettyPrintJsonString(actualObj);
        template.convertAndSend("/topic/esp24-data", newmessage);
        
        log.info(firefighter.toString());
        log.info(newmessage);
        
    }
    
    @KafkaListener(topics = "esp24_CO", groupId = "UpdateWeb", containerFactory = "UpdateWebKafkaListenerContainerFactory")
    public void COProcess(@Payload String message) {
        
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
        JsonNode actualObj = mapper.valueToTree(firefighters);
        String newmessage = prettyPrintJsonString(actualObj);
        template.convertAndSend("/topic/esp24-data", newmessage);
        
    }
    
    @KafkaListener(topics = "esp24_heartRate", groupId = "UpdateWeb", containerFactory = "UpdateWebKafkaListenerContainerFactory")
    public void HeartRateProcess(@Payload String message) {
        
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
        JsonNode actualObj = mapper.valueToTree(firefighters);
        String newmessage = prettyPrintJsonString(actualObj);
        template.convertAndSend("/topic/esp24-data", newmessage);
    }
    
    @KafkaListener(topics = "esp24_battery", groupId = "UpdateWeb", containerFactory = "UpdateWebKafkaListenerContainerFactory")
    public void BatteryProcess(@Payload String message) {
        
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
        JsonNode actualObj = mapper.valueToTree(firefighters);
        String newmessage = prettyPrintJsonString(actualObj);
        template.convertAndSend("/topic/esp24-data", newmessage);
        
    }
    
    @KafkaListener(topics = "esp24_temperature", groupId = "UpdateWeb", containerFactory = "UpdateWebKafkaListenerContainerFactory")
    public void TemperatureProcess(@Payload String message) {
        
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
        JsonNode actualObj = mapper.valueToTree(firefighters);
        String newmessage = prettyPrintJsonString(actualObj);
        template.convertAndSend("/topic/esp24-data", newmessage);
        
    }
    
    @KafkaListener(topics = "esp24_humidity", groupId = "UpdateWeb", containerFactory = "UpdateWebKafkaListenerContainerFactory")
    public void HumidityProcess(@Payload String message) {

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
        JsonNode actualObj = mapper.valueToTree(firefighters);
        String newmessage = prettyPrintJsonString(actualObj);
        template.convertAndSend("/topic/esp24-data", newmessage);
    }
    
/*
    public void updateWeb(@Payload String message) {

        JsonNode actualObj = mapper.readTree(message);
        ArrayNode datasetArray = (ArrayNode) actualObj.get("firefighters");
        ObjectNode jsonn = mapper.valueToTree(actualObj);

        for (int i = 0; i < 3; i++) {

            int coValue = Integer.parseInt(datasetArray.get(i).get("CO").asText());
            double hrValue = Double.parseDouble(datasetArray.get(i).get("hr").asText());
            int batteryValue = Integer.parseInt(datasetArray.get(i).get("bat").asText());

            if (coValue != lastValue[i][0]) {
                if (coValue > 250 || coValue <= 800) {
                    lastValue[i][0] = coValue;
                    ArrayNode alertsArray = (ArrayNode) actualObj.get("alerts");
                    ObjectNode alert = mapper.createObjectNode();
                    alert.put("id", "" + i);
                    alert.put("alert", "Firefighter " + names[i] + " is located in (" + datasetArray.get(i).get("lat").asText() + ", " + datasetArray.get(i).get("long").asText() + ") and has entered a dangerous environment!");
                    alertsArray.add(alert);
                    jsonn.putArray("alerts").addAll(alertsArray);
                    System.out.println("ALERT::::::::: " + jsonn);
                }
                if (coValue > 800) {
                    lastValue[i][0] = coValue;
                    ArrayNode alertsArray = (ArrayNode) actualObj.get("alerts");
                    ObjectNode alert = mapper.createObjectNode();
                    alert.put("id", "" + i);
                    alert.put("alert", "Firefighter " + names[i] + " is located in (" + datasetArray.get(i).get("lat").asText() + ", " + datasetArray.get(i).get("long").asText() + ") and has entered VERY dangerous environment!");
                    alertsArray.add(alert);
                    jsonn.putArray("alerts").addAll(alertsArray);
                    System.out.println("ALERT::::::::: " + jsonn);
                }
            }
            if (hrValue != lastValue[i][1]) {
                if (hrValue < 60 || hrValue <= 800) {
                    lastValue[i][1] = hrValue;
                    ArrayNode alertsArray = (ArrayNode) actualObj.get("alerts");
                    ObjectNode alert = mapper.createObjectNode();
                    alert.put("id", "" + i);
                    alert.put("alert", "Firefighter " + names[i] + " is located in (" + datasetArray.get(i).get("lat").asText() + ", " + datasetArray.get(i).get("long").asText() + ") and is probably injured or unconscious!");
                    alertsArray.add(alert);
                    jsonn.putArray("alerts").addAll(alertsArray);
                    System.out.println("ALERT::::::::: " + jsonn);
                }
            }
            if (batteryValue != lastValue[i][2]) {
                if (batteryValue < 10) {
                    lastValue[i][2] = batteryValue;
                    ArrayNode alertsArray = (ArrayNode) actualObj.get("alerts");
                    ObjectNode alert = mapper.createObjectNode();
                    alert.put("id", "" + i);
                    alert.put("alert", "Firefighter " + names[i] + " is located in (" + datasetArray.get(i).get("lat").asText() + ", " + datasetArray.get(i).get("long").asText() + "), replacement battery needed!");
                    alertsArray.add(alert);
                    jsonn.putArray("alerts").addAll(alertsArray);
                    System.out.println("ALERT::::::::: " + jsonn);
                }
            }
        }

        System.out.println("MESSAGE: " + message);
        String newmessage = prettyPrintJsonString(actualObj);
        template.convertAndSend("/topic/esp24-data", newmessage);
        System.out.println("NEW MESSAGE: " + newmessage);

    }
*/
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
