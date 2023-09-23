package com.proj.kafkachatserver.models;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Message {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sender;
    private String receiver;
    private String content;
    private long timestamp;
    private MessageType type;
    private byte[] fileContent; // for files
    private String fileName; // original file name

    
    
    public Message(String sender, String receiver, String content, long timestamp, MessageType type,
			byte[] fileContent, String fileName) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.timestamp = timestamp;
		this.type = type;
		this.fileContent = fileContent;
		this.fileName = fileName;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	public Message(String sender, String receiver,String content, long currentTimeInMillis, byte[] fileContent, String fileName) {
		super();
		this.sender = sender;
		this.receiver=receiver;
		this.content = content;
		this.timestamp = currentTimeInMillis;
		this.fileContent = fileContent;
		this.fileName = fileName;
	}

	public Message() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    
//	@Override
//	public String toString() {
//		return "Message [sender=" + sender + ", content=" + content + ", timestamp=" + timestamp + ", fileContent="
//				+ Arrays.toString(fileContent) + ", fileName=" + fileName + "]";
//	}
    

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setType(MessageType type2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return "Message [sender=" + sender + ", receiver=" + receiver + ", content=" + content + ", timestamp="
				+ timestamp + ", type=" + type + ", fileContent=" + Arrays.toString(fileContent) + ", fileName="
				+ fileName + "]";
	}

	public MessageType getType() {
		return type;
	}

  

//    @Override
//    public String toString() {
//        return "Message{" +
//                "sender='" + sender + '\'' +
//                ", content='" + content + '\'' +
//                ", timestamp='" + timestamp + '\'' +
//                '}';
//    }
}
