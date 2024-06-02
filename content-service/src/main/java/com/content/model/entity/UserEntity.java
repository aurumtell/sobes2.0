package com.content.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "users", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @Column(name = "passwordhash", unique = true)
    private String passwordHash;

    @Column(name = "username")
    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "registrationdate")
    private LocalDateTime registrationDate;

    @ManyToOne(cascade= CascadeType.MERGE)
    @JoinColumn(name = "roleid")
    private RoleEntity role;

//    @ManyToMany(mappedBy = "savedUsers", fetch= FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    private Set<MaterialEntity> savedMaterials;

//    @JsonIgnore
//    @OneToMany(mappedBy="createdBy", fetch= FetchType.EAGER)
//    private Set<ArticleEntity> article;
//
//    @ManyToMany(mappedBy = "users", fetch= FetchType.EAGER)
//    private Set<ArticleEntity> articlesReaction;
//
//    @ManyToMany(mappedBy = "savedUsers", fetch= FetchType.EAGER)
//    private Set<ArticleEntity> savedArticles;

//    @ManyToMany(fetch= FetchType.EAGER)
//    @JoinTable(
//            name = "subscribtion",
//            joinColumns = { @JoinColumn(name = "publisherid") },
//            inverseJoinColumns = { @JoinColumn(name = "subscriberid") }
//    )
//    private Set<UserEntity> subscribers = new HashSet<>();

//    @ManyToMany(mappedBy = "subscribers", fetch= FetchType.EAGER)
//    private Set<UserEntity> subscriptions = new HashSet<>();
//
//    public void addSubscriber(UserEntity user) {
//        this.subscribers.add(user);
//        user.getSubscriptions().add(this);
//    }
//
//    public void removeSubscriber(UserEntity user) {
//        this.subscribers.remove(user);
//        user.getSubscriptions().remove(this);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
//        System.out.println("equals");
//        System.out.println(user.getLogin());
//        System.out.println(this.getLogin());
//        System.out.println(user);
//        System.out.println(this);
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}


