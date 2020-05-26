package com.springKafka.datasiren.services;

import com.google.gson.Gson;
import com.springKafka.datasiren.model.Firefighter;
import com.springKafka.datasiren.model.FirefightersGroup;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Service
public class KafkaConsumerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    SimpMessagingTemplate template;

    @KafkaListener(topics = "${kafka.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload String message) {

        Gson g = new Gson();
        FirefightersGroup p = g.fromJson(message, FirefightersGroup.class);
        int id = 0;
        Iterator<Firefighter> iter = p.getFirefighters().iterator();
        while(iter.hasNext()){
        	Firefighter f  = iter.next();
            kafkaTemplate.send("esp24_GPS"          ,   id + " " + f.getLat() + " " + f.getLongi() + " " + f.getAlt());
            kafkaTemplate.send("esp24_CO"           ,   id + " " + f.getCO());
            kafkaTemplate.send("esp24_heartRate"    ,   id + " " + f.getHr());
            kafkaTemplate.send("esp24_battery"      ,   id + " " + f.getBat());
            kafkaTemplate.send("esp24_temperature"  ,   id + " " + f.getTemp());
            kafkaTemplate.send("esp24_humidity"     ,   id + " " + f.getHum());
            kafkaTemplate.send("esp24_HGT"          ,   id + " " + "-99");
            kafkaTemplate.send("esp24_pressure"     ,   id + " " + "-99");
            kafkaTemplate.send("esp24_NO2"          ,   id + " " + "-99");
            kafkaTemplate.send("esp24_luminosity"   ,   id + " " + "-99");

            id++;

        }
        template.convertAndSend("/topic/esp24-data", message);

        //log.info(message);
        //log.info(p.toString());
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
