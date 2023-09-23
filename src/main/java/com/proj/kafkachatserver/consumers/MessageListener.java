package com.proj.kafkachatserver.consumers;

import com.proj.kafkachatserver.constants.KafkaConstants;
import com.proj.kafkachatserver.models.Message;
import com.proj.kafkachatserver.models.WebSocketSessionRegistry;
import com.proj.kafkachatserver.repository.ChatMessageRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class MessageListener {
    //@Autowired
    //SimpMessagingTemplate template;
    //WebSocketSessionRegistry sessionRegistry;
    
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    
    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC
            //groupId = KafkaConstants.GROUP_ID
    )
    /*public void listen(Message message) {
        System.out.println("sending via kafka listener..");
        System.out.println(message.getSender() +"sent :"+ message.getContent());
        System.out.println("Message send to" + message.getReceiver());
        System.out.println("Message received from" + message.getSender());
        
    }*/
    
    public void listen(String message) {
        System.out.println("sending via kafka listener..");
        System.out.println(message);
    }
    /*public void consumeFileUploadMessage(String fileName) {
        Message message = new Message();
        message.setFileName(fileName);
       
        chatMessageRepository.save(message);
    }*/
        
        
        //String recipientUserId = message.getReceiver();
        //String notificationContent = message.getNotificationContent();
//        String notificationContent = "Message received from" + message.getSender();
//        WebSocketSession recipientSession = sessionRegistry.getSession(recipientUserId);
//        if (recipientSession != null) {
//            // Send the notification to the specific user's WebSocket session
//            template.convertAndSendToUser(recipientUserId, "/topic/group", notificationContent);
        
       // template.convertAndSend("/topic/group", message);
    
}