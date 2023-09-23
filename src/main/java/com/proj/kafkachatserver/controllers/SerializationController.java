package com.proj.kafkachatserver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proj.kafkachatserver.models.DataObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SerializationController {

    @Autowired
    private SerializationConfig serializationConfig;

    @Autowired
    private ObjectMapper objectMapper; // Autowire Jackson ObjectMapper

    @PostMapping("/serialize")
    public String serializeData(@RequestBody DataObject data) {
        // Use the selected serialization format (e.g., JSON)
        String format = serializationConfig.getFormat();
        if ("json".equalsIgnoreCase(format)) {
            try {
                // Serialize data to JSON
                String json = objectMapper.writeValueAsString(data);
                return json;
            } catch (Exception e) {
                e.printStackTrace();
                return "Serialization error";
            }
        } else {
            return "Unsupported serialization format";
        }
    }

    @PostMapping("/deserialize")
    public DataObject deserializeData(@RequestBody String jsonData) {
        // Use the selected serialization format (e.g., JSON)
        String format = serializationConfig.getFormat();
        if ("json".equalsIgnoreCase(format)) {
            try {
                // Deserialize JSON to object
                DataObject data = objectMapper.readValue(jsonData, DataObject.class);
                return data;
            } catch (Exception e) {
                e.printStackTrace();
                return null; // Handle deserialization error appropriately
            }
        } else {
            return null; // Unsupported format
        }
    }

	public void setSerializationConfig(SerializationConfig serializationConfig2) {
		// TODO Auto-generated method stub
		
	}

	public void setObjectMapper(ObjectMapper objectMapper2) {
		// TODO Auto-generated method stub
		
	}
}
