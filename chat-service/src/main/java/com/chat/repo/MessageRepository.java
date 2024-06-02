package com.chat.repo;

import com.chat.model.entity.ChatEntity;
import com.chat.model.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByChat(ChatEntity chat);
    void deleteByChat(ChatEntity chat);
}
