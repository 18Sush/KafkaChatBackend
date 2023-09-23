package com.proj.kafkachatserver.services;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaMessageConsumer kafkaMessageConsumer;

    @Autowired
    public KafkaConsumerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 60000)
    public void recoverPendingMessages() {
        long lastProcessedOffset = kafkaMessageConsumer.getLastProcessedOffset();

        logger.info("Recovery process started. Last processed offset: {}", lastProcessedOffset);

        List<String> pendingMessages = fetchPendingMessagesFromKafka(lastProcessedOffset);

        for (String message : pendingMessages) {
            logger.debug("Recovered message: {}", message);
            kafkaTemplate.send("pending-messages", message);
        }

        logger.info("Recovery process completed. Recovered {} messages.", pendingMessages.size());
    }

    public List<String> retrieveMessagesFromDeadLetterTopic() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "GROUP_ID");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("dead-letter"));

        List<String> deadLetterMessages = new ArrayList<>();

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

        for (ConsumerRecord<String, String> record : records) {
            deadLetterMessages.add(record.value());
        }

        consumer.close();
        return deadLetterMessages;
    }

    private List<String> fetchPendingMessagesFromKafka(long lastProcessedOffset) {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "GROUP_ID");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
        consumerProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);

        // Create a Kafka consumer
        KafkaConsumer<String, String> kafkaConsumer = (KafkaConsumer<String, String>) consumerFactory.createConsumer();

        // Create a TopicPartition object for the topic you want to seek
        TopicPartition topicPartition = new TopicPartition("pending-messages", 0);

        // Seek to the last processed offset
        kafkaConsumer.assign(Collections.singletonList(topicPartition));
        kafkaConsumer.seek(topicPartition, lastProcessedOffset);

        // Fetch and collect messages
        List<String> pendingMessages = new ArrayList<>();
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
            if (records.isEmpty()) {
                break;
            }
            for (ConsumerRecord<String, String> record : records) {
                pendingMessages.add(record.value());
            }
        }

        // Close the consumer
        kafkaConsumer.close();

        return pendingMessages;
    }
}
