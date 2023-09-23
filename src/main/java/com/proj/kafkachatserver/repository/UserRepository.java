package com.proj.kafkachatserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proj.kafkachatserver.models.User;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	int countByOnlineStatus(boolean onlineStatus);
	List<User> findByOnlineStatus(boolean onlineStatus);
}