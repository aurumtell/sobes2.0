package com.chat.model.response;

import com.chat.model.entity.ChatEntity;
import com.chat.model.entity.MessageEntity;
import com.chat.model.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MessageResponse {
    private Long messageId;
    private Long chatId;
    private LocalDateTime date;
    private UserEntity sender;
    private UserEntity responder;
    private String text;
    private Boolean isRead;

    public MessageResponse(MessageEntity message, UserEntity responder) {
        this.messageId = message.getId();
        this.chatId = message.getChat().getId();
        this.sender = message.getSender();
        this.date = message.getDate();
        this.text = message.getContent();
        this.responder = responder;
        this.isRead = message.isRead();
    }


}
