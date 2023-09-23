package com.proj.kafkachatserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proj.kafkachatserver.models.UserProfile;


public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

}
