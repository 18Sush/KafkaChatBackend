package com.proj.kafkachatserver.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.proj.kafkachatserver.models.DeadLetterMessage;
import com.proj.kafkachatserver.models.Message;
import com.proj.kafkachatserver.models.MessageType;
import com.proj.kafkachatserver.repository.ChatMessageRepository;
import com.proj.kafkachatserver.repository.deadletterrepository;

import com.proj.kafkachatserver.services.KafkaConsumerService;
import com.proj.kafkachatserver.services.KafkaMessageConsumer;
import com.proj.kafkachatserver.services.Kafkaproducer;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private  KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private  Kafkaproducer kafkaProducer;
    private  KafkaConsumerService kafkaConsumerService;
    @Autowired
    private deadletterrepository deadLetterMessageRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    
    

    @Autowired
    public AdminController(
            KafkaTemplate<String, String> kafkaTemplate,
            Kafkaproducer kafkaProducer,
            
           
            ChatMessageRepository chatMessageRepository,
            deadletterrepository deadLetterMessageRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaProducer = kafkaProducer;
        this.kafkaConsumerService = kafkaConsumerService;
        
        this.deadLetterMessageRepository = deadLetterMessageRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    @PostMapping("/send")
    public String sendNotification(
            @RequestParam("content") String content,
            @RequestParam(value = "sender", required = true) String sender)
    {
    	
            System.out.println("Received notification content: " + content);

            // Set the expiration time in milliseconds (e.g., 3600 seconds = 1 hour)
            long expirationTimeInMillis = 0 * 1000;
            long currentTimeInMillis = System.currentTimeMillis();
            long messageReceivedTimeInMillis = currentTimeInMillis; // Initialize with current time

            if (sender != null && !sender.isEmpty()) {
                // Calculate the message received time
                if (currentTimeInMillis > expirationTimeInMillis) {
                    messageReceivedTimeInMillis = currentTimeInMillis - expirationTimeInMillis;
                }

                // Check if the message is expired
                if (messageReceivedTimeInMillis < currentTimeInMillis) {
                	// Send the message to the main topic with the specified sender
                    kafkaProducer.sendWarning(sender, content, expirationTimeInMillis);
                    Message message = new Message(sender, content, null, currentTimeInMillis, null, null);
                    chatMessageRepository.save(message);
                    return "Message sent by " + sender + ": " + content + " (Received at: " + messageReceivedTimeInMillis + ")";
                    
                } else {
                	DeadLetterMessage deadLetterMessage = new DeadLetterMessage(sender, content, null);
                    deadLetterMessageRepository.save(deadLetterMessage);

                    // Send the expired message to the dead-letter queue
                    kafkaProducer.sendToDeadLetter(sender, content, currentTimeInMillis);
                    
                    notifyAdministrator("Expired message sent to dead-letter queue: " + content);
                    return "Expired message sent to dead-letter queue: " + content + " (Received at: " + messageReceivedTimeInMillis + ")";
                  
                	
                }
            } else {
                // Calculate the message received time
                if (currentTimeInMillis > expirationTimeInMillis) {
                    messageReceivedTimeInMillis = currentTimeInMillis - expirationTimeInMillis;
                }

                // Check if the message is expired
                if (messageReceivedTimeInMillis < currentTimeInMillis) {
                	// Send the message to the main topic with the specified sender
                    kafkaProducer.sendWarning(sender, content, expirationTimeInMillis);
                    Message message = new Message(sender, content, null, currentTimeInMillis, null, null);
                    chatMessageRepository.save(message);
                    return "Message sent by " + sender + ": " + content + " (Received at: " + messageReceivedTimeInMillis + ")";
                    
                    
                } else {
                	DeadLetterMessage deadLetterMessage = new DeadLetterMessage(sender, content, null);
                    deadLetterMessageRepository.save(deadLetterMessage);

                    // Send the expired message to the dead-letter queue
                    kafkaProducer.sendToDeadLetter(sender, content, currentTimeInMillis);
                    
                    notifyAdministrator("Expired message sent to dead-letter queue: " + content);
                    return "Expired message sent to dead-letter queue: " + content + " (Received at: " + messageReceivedTimeInMillis + ")";
                  
                }
            }
        }

      
   
    

    @GetMapping("/retrieve-dead-letter-messages")
    public List<DeadLetterMessage> retrieveDeadLetterMessages(@RequestParam(value = "sender") String sender) {
        // Retrieve dead-letter messages based on the sender from the deadletterrepository
        return deadLetterMessageRepository.findBySender(sender);
    }

    @PostMapping("/resubmit-dead-letter-messages")
    public void resubmitDeadLetterMessages(@RequestBody List<DeadLetterMessage> resubmitRequests) {
        for (DeadLetterMessage request : resubmitRequests) {
            String sender = request.getSender();
            String content = request.getContent();
            long timestamp = request.getTimestamp();

            // Create a new chat message and save it to the chatMessageRepository
            Message message = new Message(sender, content, null, timestamp, null, null);
            chatMessageRepository.save(message);
        }
    }

    // Helper method to check if a message belongs to a specific sender
    private boolean isMessageFromSender(String message, String sender) {
       
         return message.contains("Sender: " + sender);
    }

   /*
    @PostMapping("/sendmessage")
    public String processLog(@RequestBody String logMessage) {
        try {
            recoveryListener.processDisasterRecoveryLog(logMessage);
            return "Log processed successfully!";
        } catch (Exception e) {
            return "Error processing log: " + e.getMessage();
        }
    }
    
    @PostMapping("/send")
    public String sendMessage(@RequestBody String message) {
        // Send a message to Kafka
        kafkaMessageProducer.sendMessage(message);
        return "message sent";
    }

    @GetMapping("/get-last-offset")
    public long getLastProcessedOffset() {
        // Retrieve and return the last processed offset
        return kafkaMessageConsumer.getLastProcessedOffset();
    }

   /* @GetMapping("/get-last-offset-via-service")
    public long getLastProcessedOffsetViaService() {
        // Retrieve and return the last processed offset using the offset monitor service
        return offsetMonitorService.retrieveAndPrintLastProcessedOffset();
    }*/


    private boolean isPoisonMessage(String message) {
        // Add more keywords to detect poison messages
        String[] poisonKeywords = {"POISON", "poison", "ERROR", "error", "INVALID", "invalid","Poison"};
        for (String keyword : poisonKeywords) {
            if (message.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private void notifyAdministrator(String notification) {
        System.out.println("Administrator notified: " + notification);
    }
}