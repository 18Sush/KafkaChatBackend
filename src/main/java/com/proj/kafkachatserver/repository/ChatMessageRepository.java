package com.proj.kafkachatserver.repository;
import com.proj.kafkachatserver.models.Message;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ChatMessageRepository extends JpaRepository<Message, Long> {
	
	List<Message> findBySenderAndReceiver(String sender, String receiver);
	
	Optional<Message> findById(Long id);

	Message findTopByOrderByIdDesc();
}
