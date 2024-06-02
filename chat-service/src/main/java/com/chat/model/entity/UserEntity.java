package com.chat.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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


    @Column(name = "email", unique = true)
    private String email;


    @JsonIgnore
    @Column(name = "passwordhash", unique = true)
    private String passwordHash;

    @Column(name = "username")
    private String username;

//    @JsonIgnore
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Column(name = "registrationdate")
//    private LocalDateTime registrationDate;
//
//    @ManyToOne(cascade= CascadeType.MERGE)
//    @JoinColumn(name="levelid")
//    private LevelEntity experience;
    @JsonIgnore
    @OneToMany(mappedBy="participantone", fetch= FetchType.EAGER)
    private Set<com.chat.model.entity.ChatEntity> chatsOne = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy="participanttwo", fetch= FetchType.EAGER)
    private Set<ChatEntity> chatsTwo = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy="sender", fetch= FetchType.EAGER)
    private Set<MessageEntity> messages = new HashSet<>();

//    @JsonIgnore
//    @Column(name = "verify")
//    private boolean verified;
//
//    @JsonIgnore
//    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    @JoinTable(name="user_companies",
//            joinColumns=@JoinColumn (name="userid"),
//            inverseJoinColumns=@JoinColumn(name="companyid"))
//    @JsonManagedReference
//    private Set<com.chat.model.entity.CompanyEntity> companies = new HashSet<>();
//
//    @JsonIgnore
//    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    @JoinTable(name="user_professions",
//            joinColumns=@JoinColumn (name="userid"),
//            inverseJoinColumns=@JoinColumn(name="professionid"))
//    @JsonManagedReference
//    private Set<com.chat.model.entity.ProfessionEntity> professions = new HashSet<>();
//
//    @JsonIgnore
//    @Column(name = "code")
//    private String confirmationCode;
//
//    @JsonIgnore
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Column(name = "codesentat")
//    private LocalDateTime confirmationCodeSentAt;
//
//    public void addProfession(com.chat.model.entity.ProfessionEntity profession) {
//        this.professions.add(profession);
//        profession.getUsers().add(this);
//    }
//
//    public void removeProfession(ProfessionEntity profession) {
//        this.professions.remove(profession);
//        profession.getUsers().remove(this);
//        System.out.println("remove" + profession);
//    }
//
//    public void addCompany(com.chat.model.entity.CompanyEntity company) {
//        this.companies.add(company);
//        company.getUsers().add(this);
//    }
//
//    public void removeCompany(CompanyEntity company) {
//        this.companies.remove(company);
//        company.getUsers().remove(this);
//    }


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

