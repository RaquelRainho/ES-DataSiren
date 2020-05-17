package com.springKafka.datasiren.configurations;

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

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
	
    @Value("${kafka.bootstrapserver}")
    private String bootstrapServer;
	
    public Map<String,Object> consumerConfigs(String groupId){
            Map<String,Object> props=new HashMap<>();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

            return props;
    }
    
    public ConsumerFactory<String, String> consumerFactory(String groupId){
            return new DefaultKafkaConsumerFactory<>(consumerConfigs(groupId));
    }

    public ConcurrentKafkaListenerContainerFactory<String,String> kafkaListenerContainerFactory(String groupId){
            ConcurrentKafkaListenerContainerFactory<String, String> factory=new ConcurrentKafkaListenerContainerFactory();
            factory.setConsumerFactory(consumerFactory(groupId));
            return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> SensorProcessingKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory("SensorProcessing");
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory("esp24_AllSensorData");
    }
}
