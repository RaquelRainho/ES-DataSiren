package com.springKafka.datasiren.configurations;

import com.springKafka.datasiren.model.Location;
import com.springKafka.datasiren.model.Notification;
import com.springKafka.datasiren.model.Sensor;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.bootstrapserver}")
    private String bootstrapServer;

    //Generic
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> confProps = new HashMap<>();
        confProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        confProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        confProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(confProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    //Notifications
    @Bean
    public ProducerFactory<String, Notification> notificationProducerFactory() {
        Map<String, Object> confProps = new HashMap<>();
        confProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        confProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        confProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(confProps);
    }

    @Bean
    public KafkaTemplate<String, Notification> notificationKafkaTemplate() {
        return new KafkaTemplate<>(notificationProducerFactory());
    }

    //Sensors
    @Bean
    public ProducerFactory<String, Sensor> sensorProducerFactory() {
        Map<String, Object> confProps = new HashMap<>();
        confProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        confProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        confProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(confProps);
    }

    @Bean
    public KafkaTemplate<String, Sensor> sensorKafkaTemplate() {
        return new KafkaTemplate<>(sensorProducerFactory());
    }

    //Locations
    @Bean
    public ProducerFactory<String, Location> locationProducerFactory() {
        Map<String, Object> confProps = new HashMap<>();
        confProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        confProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        confProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(confProps);
    }

    @Bean
    public KafkaTemplate<String, Location> locationKafkaTemplate() {
        return new KafkaTemplate<>(locationProducerFactory());
    }
}
