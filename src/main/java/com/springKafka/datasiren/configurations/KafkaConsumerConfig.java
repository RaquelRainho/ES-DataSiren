package com.springKafka.datasiren.configurations;

import com.springKafka.datasiren.model.Notification;
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
    public ConcurrentKafkaListenerContainerFactory<String, String> SensorProcessingKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory("SensorProcessing");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> UpdateWebKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory("UpdateWeb");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory("esp24_AllSensorData");
    }

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
}
