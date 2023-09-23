package com.proj.kafkachatserver.controllers;

import com.proj.kafkachatserver.constants.KafkaConstants;
import org.springframework.kafka.annotation.KafkaListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.proj.kafkachatserver.models.Message;
import com.proj.kafkachatserver.models.MessageType;

import com.proj.kafkachatserver.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@RestController
public class ChatController {
	
	private final Logger logger = LoggerFactory.getLogger(ChatController.class);
	
	@Autowired
	public ChatController(KafkaTemplate<String, Message> kafkaTemplate) {
		super();
		//this.template = template;
		this.kafkaTemplate = kafkaTemplate;
		//this.userService = userService;
	}

	/*@Autowired
    SimpMessagingTemplate template;*/
	
    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;
    
    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate1;
    
    @Autowired
    private ChatMessageRepository chatMessageRepository;
   
    @PostMapping(value = "/api/send", consumes = { "multipart/form-data" })
	public ResponseEntity<String> sendMessageAndUploadFile(
	        @RequestPart("sender") String sender,
	        @RequestPart("receiver") String receiver,
	        @RequestPart("type") MessageType type,
	        @RequestPart(value = "content", required = false) String content,
	        @RequestPart(value = "file", required = false) MultipartFile file) {

	    if (file != null && !file.isEmpty() && content != null) {
	        // Handle sending both text and a file
	        try {
	            // Get the original filename and sanitize it if necessary
	            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

	            // Get the file content as a byte array
	            byte[] fileContent = file.getBytes();

	            // Create and save the message with the file
	            Message message = new Message();
	            message.setSender(sender);
	            message.setReceiver(receiver);
	            message.setType(type);
	            //message.setTimestamp(LocalDateTime.now().toString());
	            message.setContent(content);
	            message.setFileName(fileName);
	            message.setFileContent(fileContent);

	            // Send the message to Kafka
	            kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message).get();

	            // Save the message to the database
	            chatMessageRepository.save(message);

	            return ResponseEntity.status(HttpStatus.CREATED).body("Message with file sent successfully");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message with file");
	        }
	    } else if (file != null && !file.isEmpty()) {
	        // Handle sending only a file
	        try {
	            // Get the original filename and sanitize it if necessary
	            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

	            // Get the file content as a byte array
	            byte[] fileContent = file.getBytes();

	            // Create and save the message with only the file
	            Message message = new Message();
	            message.setSender(sender);
	            message.setReceiver(receiver);
	            message.setType(type);
	            //message.setTimestamp(LocalDateTime.now().toString());
	            message.setFileName(fileName);
	            message.setFileContent(fileContent);

	            // Send the message to Kafka
	            kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message).get();

	            // Save the message to the database
	            chatMessageRepository.save(message);

	            return ResponseEntity.status(HttpStatus.CREATED).body("File sent successfully");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send file");
	        }
	    } else if (content != null) {
	        // Handle sending only text
	        Message message = new Message();
	        message.setSender(sender);
	        message.setReceiver(receiver);
	        message.setType(type);
	        //message.setTimestamp(LocalDateTime.now().toString());
	        message.setContent(content);

	        try {
	            // Send the message to Kafka
	            kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message).get();

	            // Save the message to the database
	            chatMessageRepository.save(message);

	            return ResponseEntity.status(HttpStatus.CREATED).body("Message sent successfully");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message");
	        }
	    } else {
	        return ResponseEntity.badRequest().body("Invalid request. Provide either file, text, or both.");
	    }
	}
    
    @GetMapping("/api/chat-history")
	public ResponseEntity<List<Message>> getChatHistory(
	        @RequestParam("sender") String sender,
	        @RequestParam("receiver") String receiver) {

	    // Retrieve chat history from the repository
	    List<Message> chatHistory = chatMessageRepository.findBySenderAndReceiver(sender, receiver);

	    if (!chatHistory.isEmpty()) {
	        return ResponseEntity.ok(chatHistory);
	    } else {
	        return ResponseEntity.noContent().build();
	    }
	}
    
    
    /*@GetMapping("/{id}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
	    Message message = chatMessageRepository.findById(id).orElse(null);

	    if (message != null) {
	        byte[] fileContent = message.getFileContent();

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentDisposition(ContentDisposition.builder("attachment")
	                .filename(message.getFileName())
	                .build()); // Use ContentDisposition.builder to set the header

	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        headers.add("File-Download-Success", "File '" + message.getFileName() + "' downloaded successfully!");
	        return ResponseEntity.ok()
	                .headers(headers)
	                .body(fileContent);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}*/
    
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        Message message = chatMessageRepository.findById(id).orElse(null);

        if (message != null) {
            byte[] fileContent = message.getFileContent();

            // Send the file content to a Kafka topic
            kafkaTemplate1.send("kafka-chat-3", fileContent);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename(message.getFileName())
                    .build());

            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add("File-Download-Success", "File '" + message.getFileName() + "' downloaded successfully!");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

    

        @KafkaListener(topics = "kafka-chat-3", groupId = "kafka-sandbox")
        public void receiveMessage(String message) {
            // Handle the received message

            // Log the message to the separate log file
            logger.info("Received message: " + message);
        }

       
	
}
