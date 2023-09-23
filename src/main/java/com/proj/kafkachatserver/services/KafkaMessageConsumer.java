package com.proj.kafkachatserver.services;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class KafkaMessageConsumer {

    private long lastProcessedOffset = -1;

    public long getLastProcessedOffset() {
        return lastProcessedOffset;
    }

    public void setLastProcessedOffset(long lastProcessedOffset) {
        this.lastProcessedOffset = lastProcessedOffset;
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Define a setter method for KafkaTemplate
    public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${kafka.topic}")
    public void consume(String message, @Header("kafka_offset") Long offset) {
        // Process the incoming message
        System.out.println("Received message: " + message);

        // Update the last successfully processed offset
        lastProcessedOffset = offset;
    }

    public void retrieveAndPrintLastProcessedOffset() {
        // You can use the KafkaTemplate here if needed
        if (kafkaTemplate != null) {
            kafkaTemplate.send("some-topic", "some-message");
        }

        long lastProcessedOffset = getLastProcessedOffset();
        System.out.println("Last processed offset: " + lastProcessedOffset);
    }
}
