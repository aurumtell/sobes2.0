package com.chat.service;

import com.chat.exception.BusinessException;
import com.chat.exception.MyEntityNotFoundException;
import com.chat.model.entity.ChatEntity;
import com.chat.model.entity.MessageEntity;
import com.chat.model.entity.UserEntity;
import com.chat.model.request.Message;
import com.chat.model.response.MessageResponse;
import com.chat.repo.ChatRepository;
import com.chat.repo.MessageRepository;
import com.chat.repo.UserRepository;
import com.chat.security.services.UserDetailsImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
public class MessageService {
    @Autowired
    ChatRepository chatRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public ChatEntity createChat(Long userId, UserDetailsImpl user) {
        System.out.println("create chat service");
        ChatEntity chat = new ChatEntity();
        UserEntity participantOne = userRepository.findById(user.getId()).get();
        System.out.println(user.getId());
        System.out.println(participantOne);
        UserEntity participantTwo = userRepository.findById(userId).orElseThrow(() -> new MyEntityNotFoundException("user"));
        if (chatRepository.existsByParticipantoneAndParticipanttwo(participantOne, participantTwo)) {
            throw new BusinessException("Chat already exists!");
        }
        chat.setParticipantone(participantOne);
        chat.setParticipanttwo(participantTwo);
        System.out.println("chat save");
        return chatRepository.save(chat);
    }

    @Transactional
    public MessageResponse sendMessage(Message message) {
        System.out.println("sender" + message.getSenderId());

        // Получение отправителя
        UserEntity sender = userRepository.findById(message.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid sender ID"));
        System.out.println("sender find");

        // Получение чата
        ChatEntity chat = chatRepository.findById(message.getChatId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat ID"));
        System.out.println("chat find");

        // Явная инициализация ленивой коллекции, если это необходимо
        // entityManager.refresh(chat);

        System.out.println("chat" + chat.getId());
        System.out.println("sender" + sender.getId());

        // Создание нового сообщения
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setChat(chat);
        messageEntity.setSender(sender);
        messageEntity.setContent(message.getText());
        System.out.println("message" + message.getText());
        messageEntity.setDate(LocalDateTime.now());

        // Сохранение сообщения
        MessageEntity savedMessage = messageRepository.save(messageEntity);
        System.out.println("save message");

        return new MessageResponse(savedMessage, getRecipient(chat, sender));
    }

    public List<MessageResponse> getMessages(Long chatId) {
        ChatEntity chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat ID"));

        return messageRepository.findByChat(chat).stream()
                .map(message -> toResponse(message, chat))
                .collect(Collectors.toList());
    }


    private MessageResponse toResponse(MessageEntity message, ChatEntity chat) {
        UserEntity recipient = getRecipient(chat, message.getSender());
        return new MessageResponse(message, recipient);
    }

    public List<ChatEntity> getChats(UserDetailsImpl user) {
        return chatRepository.findByParticipantoneOrParticipanttwo(userRepository.findById(user.getId()).get(), userRepository.findById(user.getId()).get());
    }

    private UserEntity getRecipient(ChatEntity chat, UserEntity sender) {
        if (chat.getParticipantone().equals(sender)) {
            return chat.getParticipanttwo();
        } else if (chat.getParticipanttwo().equals(sender)) {
            return chat.getParticipantone();
        } else {
            throw new IllegalArgumentException("Sender is not part of this chat");
        }
    }

    @Transactional
    public List<MessageEntity> markMessagesAsRead(List<Long> messageIds) {
        List<MessageEntity> messages = messageRepository.findAllById(messageIds);
        messages.forEach(message -> message.setRead(true));
        return messageRepository.saveAll(messages);

    }

    @Transactional
    public Long deleteChat(Long chatId) {
        ChatEntity chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat ID"));
        messageRepository.deleteByChat(chat);
        chatRepository.delete(chat);
        return chatId;
    }
}

