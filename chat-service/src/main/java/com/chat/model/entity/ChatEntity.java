package com.chat.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "chats", schema = "public")
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "participantoneid")
    private UserEntity participantone;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "participanttwoid")
    private UserEntity participanttwo;

    @JsonIgnore
    @OneToMany(mappedBy="chat", fetch= FetchType.LAZY)
    private Set<MessageEntity> messages = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatEntity)) return false;
        return id != null && id.equals(((ChatEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
