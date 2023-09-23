package com.proj.kafkachatserver.models;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String ipAddress;
    private boolean online; // New field for machine status

    public Machine() {
        // Default constructor
    }

    public Machine(String name, String ipAddress) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.online = false; // Default to offline status
    }

    // Getters and setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "Machine [id=" + id + ", name=" + name + ", ipAddress=" + ipAddress + ", online=" + online + "]";
    }

	public Object getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}
}

