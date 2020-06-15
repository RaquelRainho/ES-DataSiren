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
        return new NewTopic("esp24_AllSensorData", 5, (short) 1);
    }

    @Bean
    public NewTopic sensor_1() {
        return new NewTopic("esp24_CO_v2", 5, (short) 1);
    }

    @Bean
    public NewTopic sensor_2() {
        return new NewTopic("esp24_temperature_v2", 5, (short) 1);
    }

    @Bean
    public NewTopic sensor_3() {
        return new NewTopic("esp24_humidity_v2", 5, (short) 1);
    }

    @Bean
    public NewTopic sensor_4() {
        return new NewTopic("esp24_battery_v2", 5, (short) 1);
    }

    @Bean
    public NewTopic sensor_5() {
        return new NewTopic("esp24_GPS_v2", 5, (short) 1);
    }

    @Bean
    public NewTopic sensor_6() {
        return new NewTopic("esp24_heartRate_v2", 5, (short) 1);
    }

    @Bean
    public NewTopic notifications() {
        return new NewTopic("esp24_notifications_v2", 5, (short) 1);
    }

    @Bean
    public NewTopic firefightersNames() {
        return new NewTopic("esp24_firefightersNames", 5, (short) 1);
    }
}
