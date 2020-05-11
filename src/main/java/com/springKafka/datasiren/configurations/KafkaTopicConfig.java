package com.springKafka.datasiren.configurations;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

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
        return TopicBuilder.name("esp24_AllSensorData")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic sensor_1() {
        return TopicBuilder.name("esp24_CO")
                .partitions(1)
                .replicas(1)
                .build();
    }
    
    @Bean
    public NewTopic sensor_2() {
        return TopicBuilder.name("esp24_temperature")
                .partitions(1)
                .replicas(1)
                .build();
    }
    
    @Bean
    public NewTopic sensor_3() {
        return TopicBuilder.name("esp24_HGT")
                .partitions(1)
                .replicas(1)
                .build();
    }
    
    @Bean
    public NewTopic sensor_4() {
        return TopicBuilder.name("esp24_pressure")
                .partitions(1)
                .replicas(1)
                .build();
    }
    
    @Bean
    public NewTopic sensor_5() {
        return TopicBuilder.name("esp24_NO2")
                .partitions(1)
                .replicas(1)
                .build();
    }
    
    @Bean
    public NewTopic sensor_6() {
        return TopicBuilder.name("esp24_humidity")
                .partitions(1)
                .replicas(1)
                .build();
    }
    
    @Bean
    public NewTopic sensor_7() {
        return TopicBuilder.name("esp24_luminosity")
                .partitions(1)
                .replicas(1)
                .build();
    }
    
    @Bean
    public NewTopic sensor_8() {
        return TopicBuilder.name("esp24_battery")
                .partitions(1)
                .replicas(1)
                .build();
    }
    
    @Bean
    public NewTopic sensor_9() {
        return TopicBuilder.name("esp24_GPS")
                .partitions(1)
                .replicas(1)
                .build();
    }
    
    @Bean
    public NewTopic notifications() {
        return TopicBuilder.name("esp24_notifications")
                .partitions(1)
                .replicas(1)
                .build();
    }
    
        @Bean
    public NewTopic logging() {
        return TopicBuilder.name("esp24_logs")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
