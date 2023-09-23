package com.proj.kafkachatserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proj.kafkachatserver.models.DeadLetterMessage;

import org.springframework.stereotype.Repository;

@Repository
public interface deadletterrepository extends JpaRepository<DeadLetterMessage, Long>  {

	//void save(com.proj.kafkachatserver.models.DeadLetterMessage deadLetterMessage);


	List<com.proj.kafkachatserver.models.DeadLetterMessage> findBySender(String sender);

}
