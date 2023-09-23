package com.proj.kafkachatserver.services;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.proj.kafkachatserver.repository.deadletterrepository;

@Component
public class Kafkaproducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    

    
    @Value("kafka-chat-3")
    private String regularTopic;
    @Value("dead-letter")
    private String deadLetterTopic; 

    /*@Value("${kafka.topic}")
    private String kafkaTopic;*/
    @Autowired
    public Kafkaproducer(KafkaTemplate<String, String> kafkaTemplate)
    {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(String message) {
        kafkaTemplate.send(regularTopic, message);
    }
    public void sendToDeadLetter(String sender, String content, long timestamp) {
    	
        String message = "Sender: " + sender + ", Content: " + content + ", Timestamp: " + timestamp;
        kafkaTemplate.send(deadLetterTopic, message);
      
    }

    public void sendWarning(String sender, String content, long expirationTimeInSeconds) {
        // Implement logic to send the warning message to the main topic
        // Construct the message and send it
        String message = "Sender: " + sender + ", Content: " + content + ", ExpirationTimeInSeconds: " + expirationTimeInSeconds;
        kafkaTemplate.send(regularTopic, message);
    }

    public void resubmitDeadLetterWithTimestamp(String sender, String content, long timestamp) {
        // Implement logic to resubmit a dead letter message with a timestamp
        // Construct the message and send it
        String message = "Sender: " + sender + ", Content: " + content + ", Timestamp: " + timestamp;
        kafkaTemplate.send(regularTopic, message);
    }

    
    public void sendWarningToAdmin(String message) {
        String adminNotification = "Admin Warning: " + message;
        kafkaTemplate.send(regularTopic, adminNotification);
    }
    public void notifyAdminOnDeadLetter(String message) {
        String adminNotification = "Dead Letter Queue received poison message: " + message;
        kafkaTemplate.send(regularTopic, adminNotification);
    }
    public String getDeadLetterTopic() {
        return deadLetterTopic;
    }

	
}

