package com.iaschowrai.kafka_mailservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    // Configures the consumer factory for Kafka
    @Bean
    public ConsumerFactory<Long, Long> consumerFactory(){
        Map<String, Object> consumerConfigProps = new HashMap<>();
        consumerConfigProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        consumerConfigProps.put(ConsumerConfig.GROUP_ID_CONFIG, "emailMessageTopic");

        consumerConfigProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        consumerConfigProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,LongDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(consumerConfigProps);
    }

    // Configures the listener container factory for concurrent message consumption
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, Long> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<Long, Long> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
