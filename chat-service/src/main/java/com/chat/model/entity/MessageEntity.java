package com.chat.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "messages", schema = "public")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "chatid")
    private ChatEntity chat;


    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "senderid")
    private UserEntity sender;

    @Column(name = "content")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "datemessage")
    private LocalDateTime date;

    @Column(name = "isread")
    private boolean isRead;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageEntity)) return false;
        return id != null && id.equals(((MessageEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
