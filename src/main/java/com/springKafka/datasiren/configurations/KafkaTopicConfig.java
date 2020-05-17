package com.springKafka.datasiren.configurations;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.bootstrapserver}")
    private String bootstrapServer;

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        return new KafkaAdmin(configs);
    }
    
    @Bean
    public NewTopic sensor_all() {
        return new NewTopic("esp24_AllSensorData", 1, (short) 1);
    }

    @Bean
    public NewTopic sensor_1() {
        return new NewTopic("esp24_CO", 1, (short) 1);
    }
    
    @Bean
    public NewTopic sensor_2() {
        return new NewTopic("esp24_temperature", 1, (short) 1);
    }
    
    @Bean
    public NewTopic sensor_3() {
        return new NewTopic("esp24_HGT", 1, (short) 1);
    }
    
    @Bean
    public NewTopic sensor_4() {
        return new NewTopic("esp24_pressure", 1, (short) 1);
    }
    
    @Bean
    public NewTopic sensor_5() {
        return new NewTopic("esp24_NO2", 1, (short) 1);
    }
    
    @Bean
    public NewTopic sensor_6() {
        return new NewTopic("esp24_humidity", 1, (short) 1);
    }
    
    @Bean
    public NewTopic sensor_7() {
        return new NewTopic("esp24_luminosity", 1, (short) 1);
    }
    
    @Bean
    public NewTopic sensor_8() {
        return new NewTopic("esp24_battery", 1, (short) 1);
    }
    
    @Bean
    public NewTopic sensor_9() {
        return new NewTopic("esp24_GPS", 1, (short) 1);
    }
    
    @Bean
    public NewTopic sensor_10() {
        return new NewTopic("esp24_heartRate", 1, (short) 1);
    }
    
    @Bean
    public NewTopic notifications() {
        return new NewTopic("esp24_notifications", 1, (short) 1);
    }
}
