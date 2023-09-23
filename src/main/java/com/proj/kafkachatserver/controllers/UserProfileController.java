package com.proj.kafkachatserver.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.proj.kafkachatserver.models.UserProfile;
import com.proj.kafkachatserver.repository.UserProfileRepository;

@RestController
@RequestMapping("/api/profile")

public class UserProfileController {
	
	@Autowired
    private UserProfileRepository userProfileRepository;
    
    @PostMapping("/create")
    public ResponseEntity<UserProfile> createUser(@RequestBody UserProfile user) {
        UserProfile createdUser = userProfileRepository.save(user);
        return ResponseEntity.ok(createdUser);
    }
    
    @PutMapping("/{userId}/update")
    public ResponseEntity<UserProfile> updateUser(
            @PathVariable Long userId,
            @RequestBody UserProfile updatedUser) {
        java.util.Optional<UserProfile> optionalUser = userProfileRepository.findById(userId);
        if (optionalUser.isPresent()) {
            UserProfile user = optionalUser.get();
            user.setName(updatedUser.getName());
            user.setContactInformation(updatedUser.getContactInformation());
            user.setProfilePicture(updatedUser.getProfilePicture());
            UserProfile savedUser = userProfileRepository.save(user);
            return ResponseEntity.ok(savedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<?> deleteProfilePicture(@PathVariable Long userId) {
        // Check if the user exists
        java.util.Optional<UserProfile> optionalUser = userProfileRepository.findById(userId);
        if (optionalUser.isPresent()) {
            UserProfile user = optionalUser.get();
            byte[] profilePicture = user.getProfilePicture();
            if (profilePicture != null) {
                // Delete the profile picture
                user.setProfilePicture(null);
                userProfileRepository.save(user);
                return ResponseEntity.ok("Profile picture deleted successfully.");
            } else {
                return ResponseEntity.ok("Profile picture not found.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{userId}/upload-profile-picture")
    public ResponseEntity<?> uploadProfilePicture(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        // Check if the user exists
        java.util.Optional<UserProfile> optionalUser = userProfileRepository.findById(userId);
        if (optionalUser.isPresent()) {
            UserProfile user = optionalUser.get();
            try {
                // Validate and save the image
                if (!file.isEmpty() && isImageValid(file)) {
                    user.setProfilePicture(file.getBytes());
                    userProfileRepository.save(user);
                    return ResponseEntity.ok("Profile picture uploaded successfully.");
                } else {
                    return ResponseEntity.badRequest().body("Invalid image format.");
                }
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading profile picture.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserInfo(@PathVariable Long userId) {
        // Check if the user exists
        java.util.Optional<UserProfile> optionalUser = userProfileRepository.findById(userId);
        if (optionalUser.isPresent()) {
            UserProfile user = optionalUser.get();
            return ResponseEntity.ok(user);
        } else {
            // User not found
            return ResponseEntity.notFound().build();
        }
    }



    private boolean isImageValid(MultipartFile file) {
        // Get the file's original filename
        String originalFilename = file.getOriginalFilename();
        
        // Check if the original filename is not null and ends with a valid image extension
        if (originalFilename != null && (originalFilename.toLowerCase().endsWith(".jpg") || originalFilename.toLowerCase().endsWith(".jpeg"))) {
            return true; // Valid image file
        }
        
        return false; // Invalid image file
    }

}
