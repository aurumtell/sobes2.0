package com.chat.model.response;

import com.chat.model.entity.ChatEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatResponse {
    private Long id;
    private Long participantOne;
    private Long participantTwo;

    public ChatResponse(ChatEntity chat) {
        this.id = chat.getId();
        this.participantOne = chat.getParticipantone().getId();
        this.participantTwo = chat.getParticipanttwo().getId();
    }

    public static List<ChatResponse> toChatList(List<ChatEntity> chatEntities) {
        return chatEntities.stream()
                .map(ChatResponse::new)
                .collect(Collectors.toList());
    }
}
