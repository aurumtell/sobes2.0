package com.chat.repo;

import com.chat.model.entity.ChatEntity;
import com.chat.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    boolean existsById(Long id);
    boolean existsByParticipantoneAndParticipanttwo(UserEntity participantOne, UserEntity participantTwo);

    List<ChatEntity> findByParticipantoneOrParticipanttwo(UserEntity participant, UserEntity participanttwo);
}