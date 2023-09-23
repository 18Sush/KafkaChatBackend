package com.proj.kafkachatserver.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class UserProfile {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String name;
	    private String contactInformation;
	    @Lob
	    private byte[] profilePicture;
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
		public String getContactInformation() {
			return contactInformation;
		}
		public void setContactInformation(String contactInformation) {
			this.contactInformation = contactInformation;
		}
		public byte[] getProfilePicture() {
			return profilePicture;
		}
		public void setProfilePicture(byte[] profilePicture) {
			this.profilePicture = profilePicture;
		}
		public UserProfile(Long id, String name, String contactInformation, byte[] profilePicture) {
			super();
			this.id = id;
			this.name = name;
			this.contactInformation = contactInformation;
			this.profilePicture = profilePicture;
		}
		public UserProfile() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
}