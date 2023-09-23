package com.rest;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.proj.kafkachatserver.controllers.ChatController;
import com.proj.kafkachatserver.repository.ChatMessageRepository;
import com.proj.kafkachatserver.models.Message;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.*;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatControllerTests {

    @Autowired
    private ChatController chatController;

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
    }

    @Test
    public void testSendMessageWithFile() throws Exception {
        // Mock your dependencies if needed
        when(chatMessageRepository.save(Mockito.any())).thenReturn(new Message());

        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "File content".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/send")
                .file(file)
                .param("sender", "sender")
                .param("receiver", "receiver")
                .param("type", "TEXT")
                .param("content", "Hello, World!"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testGetChatHistory() throws Exception {
        // Mock your dependencies if needed
        when(chatMessageRepository.findBySenderAndReceiver("sender", "receiver"))
                .thenReturn(Arrays.asList(new Message(), new Message())); // Provide sample chat history

        mockMvc.perform(get("/api/chat-history")
                .param("sender", "sender")
                .param("receiver", "receiver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].sender").exists())
                .andExpect(jsonPath("$[0].receiver").exists())
                .andExpect(jsonPath("$[0].timestamp").exists());
    }

    @Test
    public void testDownloadFile() throws Exception {
        // Mock your dependencies if needed
        Message message = new Message();
        //message.setId(1L);
        message.setFileName("test.txt");
        message.setFileContent("File content".getBytes());

        when(chatMessageRepository.findById(1L)).thenReturn(Optional.of(message));

        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.txt\""))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().string("File content"));
    }
    


}