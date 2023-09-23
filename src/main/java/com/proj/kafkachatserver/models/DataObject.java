package com.proj.kafkachatserver.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataObject {
    private String message;

    // Getter for the 'name' field
    public String getmessage() {
        return message;
    }

    // Setter for the 'name' field
    public void setmessage(String message) {
        this.message = message;
    }

   

}
