package com.springKafka.datasiren.configurations;

import com.springKafka.datasiren.model.Location;
import com.springKafka.datasiren.model.Notification;
import com.springKafka.datasiren.model.Sensor;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${kafka.bootstrapserver}")
    private String bootstrapServer;

    //Generic
    public ConsumerFactory<String, String> consumerFactory(String groupId) {
        Map<String, Object> confprops = new HashMap<>();
        confprops.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        confprops.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        confprops.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        confprops.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        confprops.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return new DefaultKafkaConsumerFactory<>(confprops);
    }

    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(String groupId) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory(groupId));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory("esp24_AllSensorData");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> senskafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory("SensorProcessing");
    }

    //Notifications
    public ConsumerFactory<String, Notification> notificationConsumerFactory(String groupId) {
        Map<String, Object> confprops = new HashMap<>();
        confprops.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        confprops.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        confprops.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return new DefaultKafkaConsumerFactory<>(confprops, new StringDeserializer(), new JsonDeserializer<>(Notification.class));
    }

    public ConcurrentKafkaListenerContainerFactory<String, Notification> notificationKafkaListenerContainerFactory(String groupId) {
        ConcurrentKafkaListenerContainerFactory<String, Notification> factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(notificationConsumerFactory(groupId));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Notification> notificationkafkaListenerContainerFactory() {
        return notificationKafkaListenerContainerFactory("UpdateWeb");
    }

    //Sensors
    public ConsumerFactory<String, Sensor> sensorConsumerFactory(String groupId) {
        Map<String, Object> confprops = new HashMap<>();
        confprops.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        confprops.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        confprops.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return new DefaultKafkaConsumerFactory<>(confprops, new StringDeserializer(), new JsonDeserializer<>(Sensor.class));
    }

    public ConcurrentKafkaListenerContainerFactory<String, Sensor> sensorKafkaListenerContainerFactory(String groupId) {
        ConcurrentKafkaListenerContainerFactory<String, Sensor> factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(sensorConsumerFactory(groupId));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Sensor> sensorkafkaListenerContainerFactory() {
        return sensorKafkaListenerContainerFactory("UpdateWeb");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Sensor> sensorProcessingKafkaListenerContainerFactory() {
        return sensorKafkaListenerContainerFactory("SensorProcessing");
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Sensor> mainDatabaseSensorsKafkaListenerContainerFactory() {
        return sensorKafkaListenerContainerFactory("mainDatabase");
    }

    //Locations
    public ConsumerFactory<String, Location> locationConsumerFactory(String groupId) {
        Map<String, Object> confprops = new HashMap<>();
        confprops.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        confprops.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        confprops.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return new DefaultKafkaConsumerFactory<>(confprops, new StringDeserializer(), new JsonDeserializer<>(Location.class));
    }

    public ConcurrentKafkaListenerContainerFactory<String, Location> locationKafkaListenerContainerFactory(String groupId) {
        ConcurrentKafkaListenerContainerFactory<String, Location> factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(locationConsumerFactory(groupId));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Location> locationkafkaListenerContainerFactory() {
        return locationKafkaListenerContainerFactory("UpdateWeb");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Location> locationProcessingKafkaListenerContainerFactory() {
        return locationKafkaListenerContainerFactory("SensorProcessing");
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Location> mainDatabaseLocationsKafkaListenerContainerFactory() {
        return locationKafkaListenerContainerFactory("mainDatabase");
    }
}
